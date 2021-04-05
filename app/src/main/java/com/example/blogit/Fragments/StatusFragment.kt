package com.example.blogit.Fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.blogit.Model.StatusInfo
import com.example.blogit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.fragment_status.*
import kotlinx.android.synthetic.main.fragment_uploadimage_status_dialog.*
import java.io.InputStream
import java.util.*

class StatusFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var filePath: Uri
    private lateinit var bitmap: Bitmap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        toLoadUploadStatusFragmentBtn.setOnClickListener {
            val dialogPlus = DialogPlus.newDialog(context)
                .setContentHolder(ViewHolder(R.layout.fragment_uploadimage_status_dialog))
                .setGravity(Gravity.CENTER).create()

            val myViewTwo: View = dialogPlus.holderView
            val imageViewUpload: ImageView = myViewTwo.findViewById(R.id.imageViewUpload)
            val browseBtn: Button = myViewTwo.findViewById(R.id.browseBtn)
            val enterStatus: EditText = myViewTwo.findViewById(R.id.enterStatus)
            val uploadBtn: Button = myViewTwo.findViewById(R.id.uploadBtn)
            val closeBtn: Button = myViewTwo.findViewById(R.id.closeBtn)

            dialogPlus.show()

            closeBtn.setOnClickListener {
                dialogPlus.dismiss()
            }

            browseBtn.setOnClickListener {
                Dexter.withActivity(context as Activity?)
                    .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            val intent = Intent(Intent.ACTION_PICK)
                            intent.type = "image/*"
                            startActivityForResult(Intent.createChooser(intent, "Select Image File"), 1)
                        }

                        override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                            token?.continuePermissionRequest()
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {

                        }
                    }).check()
            }

            uploadBtn.setOnClickListener {
                val progressDialog = ProgressDialog(context as Activity?)
                progressDialog.setTitle("Status uploader")
                progressDialog.show()

                val storage: FirebaseStorage = FirebaseStorage.getInstance()
                val uploader: StorageReference =
                    storage.getReference("Image1" + Random().nextInt(30))

                uploader.putFile(filePath)
                    .addOnSuccessListener {
                        //got uri
                        uploader.downloadUrl.addOnSuccessListener {
                            progressDialog.dismiss()

                            val db = FirebaseFirestore.getInstance()

                            val userID = auth.currentUser?.uid
                            val statustext = enterStatus.editableText.toString()

                            val statusInfo = StatusInfo(userID, statustext, it.toString())

                            db.collection("Status Info").document(userID!!)
                                .set(statusInfo).addOnSuccessListener {
                                    Log.d("Status Info Check", "signUpUserValidation: success")
                                }
                                .addOnFailureListener {
                                    Log.d("Status Info Check", "signUpUserValidation: failure")
                                }

                            enterStatus.setText("")

                            imageViewUpload.setImageResource(R.drawable.ic_baseline_file_upload_24)
                            Toast.makeText(context as Activity?, "Status uploaded!", Toast.LENGTH_SHORT).show()
                            enterStatus.clearFocus()
                        }
                    }
                    .addOnProgressListener {
                        val percent = (100 * it.bytesTransferred) / it.totalByteCount
                        progressDialog.setMessage("Upload Percent : $percent%")
                    }
            }
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK) {
            filePath = data?.data!!                              //image uri is in filepath
            try {
                val inputStream: InputStream? = context!!.contentResolver.openInputStream(filePath)
                bitmap = BitmapFactory.decodeStream(inputStream)
                imageViewUpload.setImageBitmap(bitmap)              //setting image view with the selected image
            } catch (ex: Exception) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    //-------------------------------------------------------------------------------------------------------------------------
}