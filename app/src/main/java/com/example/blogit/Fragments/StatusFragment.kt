package com.example.blogit.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blogit.Adapters.StatusAdapter
import com.example.blogit.Adapters.StatusAdapterTwo
import com.example.blogit.Model.StatusInfo
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
import kotlinx.android.synthetic.main.fragment_uploadimage_status_dialog.view.*
import kotlinx.android.synthetic.main.status_item_view.*
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class StatusFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var filePath: Uri
    private lateinit var bitmap: Bitmap

    lateinit var recyclerViewStatusList : RecyclerView
    lateinit var recyclerViewOthersStatusList : RecyclerView
    lateinit var statusAdapter: StatusAdapter
    lateinit var statusAdapterTwo: StatusAdapterTwo
    lateinit var manager : LinearLayoutManager
    lateinit var managerTwo : LinearLayoutManager

    private var firebaseUser: FirebaseUser? = null

    lateinit var myViewTwo: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_status,container,false)
        auth = FirebaseAuth.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser

        recyclerViewStatusList = view.findViewById(R.id.recyclerViewStatusList)
        recyclerViewOthersStatusList = view.findViewById(R.id.recyclerViewOthersStatusList)

        manager = LinearLayoutManager(context)
        managerTwo = LinearLayoutManager(context)

        recyclerViewStatusList.layoutManager = manager
        recyclerViewOthersStatusList.layoutManager = managerTwo

        loadDataIntoRecycler()

        return view
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun loadDataIntoRecycler() {

        val userID = auth.currentUser?.uid

        val db = FirebaseFirestore.getInstance()

        val query: Query = db.collection("Status Info").whereEqualTo("userID", userID).orderBy("creationTime",Query.Direction.DESCENDING)
        val options: FirestoreRecyclerOptions<StatusInfo> = FirestoreRecyclerOptions.Builder<StatusInfo>()
            .setQuery(query, StatusInfo::class.java).build()

        statusAdapter = StatusAdapter(options)
        recyclerViewStatusList.adapter = statusAdapter

        val queryTwo: Query = db.collection("Status Info").whereNotEqualTo("userID", userID)
        val optionsTwo: FirestoreRecyclerOptions<StatusInfo> = FirestoreRecyclerOptions.Builder<StatusInfo>()
            .setQuery(queryTwo, StatusInfo::class.java).build()

        statusAdapterTwo = StatusAdapterTwo(optionsTwo)
        recyclerViewOthersStatusList.adapter = statusAdapterTwo
    }

    override fun onStart() {
        super.onStart()
        statusAdapter.startListening()
        statusAdapterTwo.startListening()
    }

    override fun onStop() {
        super.onStop()
        statusAdapter.stopListening()
        statusAdapterTwo.stopListening()
    }
    //-------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toLoadUploadStatusFragmentBtn.setOnClickListener {
            val dialogPlus = DialogPlus.newDialog(context)
                .setContentHolder(ViewHolder(R.layout.fragment_uploadimage_status_dialog))
                .setGravity(Gravity.CENTER).create()

            myViewTwo = dialogPlus.holderView
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

                val randomKey : String = UUID.randomUUID().toString()

                val storage: FirebaseStorage = FirebaseStorage.getInstance()
                val uploader: StorageReference = storage.getReference("StatusPics/${firebaseUser!!.uid}/${randomKey}")

                uploader.putFile(filePath)
                    .addOnSuccessListener {
                        //got uri
                        uploader.downloadUrl.addOnSuccessListener {
                            progressDialog.dismiss()

                            val db = FirebaseFirestore.getInstance()
                            val calendar : Calendar = Calendar.getInstance()
                            val simpleDateFormat = SimpleDateFormat("dd-MM / hh:mm a")
                            val dateTime : String = simpleDateFormat.format(calendar.time)

                            db.collection("User Profiles").document(firebaseUser!!.uid)
                                .get().addOnSuccessListener { documentSnapshot ->
                                    val userInfo: UserInfo? =
                                        documentSnapshot.toObject(UserInfo::class.java)
                                    val userID = auth.currentUser?.uid
                                    val statustext = enterStatus.editableText.toString()
                                    val timestamp = dateTime
                                    val statusUploadedName = userInfo!!.fullName

                                    val statusInfo = StatusInfo(userID, statustext, timestamp, it.toString(), statusUploadedName)

                                    db.collection("Status Info").document()
                                        .set(statusInfo).addOnSuccessListener {
                                            Log.d("Status Info Check", "Status: success")
                                        }
                                        .addOnFailureListener {
                                            Log.d("Status Info Check", "Status: failure")
                                        }
                                    enterStatus.setText("")
                                    imageViewUpload.setImageResource(R.drawable.ic_baseline_file_upload_24)
                                    Toast.makeText(context as Activity?, "Status uploaded!", Toast.LENGTH_SHORT).show()
                                    dialogPlus.dismiss()
                                }
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
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(filePath)
                bitmap = BitmapFactory.decodeStream(inputStream)
                myViewTwo.imageViewUpload.setImageBitmap(bitmap)              //setting image view with the selected image
            } catch (ex: Exception) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    //-------------------------------------------------------------------------------------------------------------------------
}