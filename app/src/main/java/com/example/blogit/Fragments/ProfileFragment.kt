package com.example.blogit.Fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.blogit.Model.StatusInfo
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
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
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_groupmessage.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.forgot_password_dialog.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_uploadimage_status_dialog.view.*
import kotlinx.android.synthetic.main.profile_image_upload_dialog.view.*
import kotlinx.android.synthetic.main.status_item_view.*
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore

    lateinit var myViewTwo: View

    private lateinit var filePath: Uri
    private lateinit var bitmap: Bitmap

    lateinit var user_profile_photo : CircleImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        user_profile_photo = view.findViewById(R.id.user_profile_photo)

        val userID = auth.currentUser?.uid
        val documentReference: DocumentReference = fStore.collection("User Profiles").document(userID!!)

        documentReference.addSnapshotListener(object : EventListener<DocumentSnapshot> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                val userInfo: UserInfo? = value?.toObject(UserInfo::class.java)
                user_profile_name?.text = value?.getString("fullName")
                user_bio?.text = value?.getString("status")
                user_email?.text = value?.getString("emailId")
                user_age?.text = value?.getString("age")
                user_phone?.text = value?.getString("phoneNumber")
                if(isAdded) {
                    Glide.with(context!!)
                        .load(userInfo!!.profileimage)
                        .error(R.drawable.blank_profile_picture)
                        .into(user_profile_photo);
                }
            }
        })

        user_edit_info.setOnClickListener {
            editUserInformation()
        }

        user_profile_photo.setOnClickListener {
            addProfilePhoto()
        }
    }
    //------------------------------------------------------------------------------------------------------------------------- --
    private fun editUserInformation() {
        val dialogPlus = DialogPlus.newDialog(context)
            .setContentHolder(ViewHolder(R.layout.edit_user_info_dialog))
            .setGravity(Gravity.CENTER).create()

        val myView: View = dialogPlus.holderView
        val fullName: EditText = myView.findViewById(R.id.editEnterFullName)
        val age: EditText = myView.findViewById(R.id.editEnterAge)
        val phoneNumber: EditText = myView.findViewById(R.id.editEnterPhonenumber)
        val status: EditText = myView.findViewById(R.id.editEnterStatus)
        val save: Button = myView.findViewById(R.id.btn_save)
        val close: Button = myView.findViewById(R.id.btn_close)

        dialogPlus.show()

        val userID = auth.currentUser?.uid
        val documentReference: DocumentReference = fStore.collection("User Profiles").document(userID!!)

        documentReference.addSnapshotListener(object : EventListener<DocumentSnapshot> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                fullName.setText(value?.getString("fullName"))
                age.setText( value?.getString("age"))
                phoneNumber.setText(value?.getString("phoneNumber"))
                status.setText(value?.getString("status"))
            }
        })

        close.setOnClickListener {
            dialogPlus.dismiss()
        }

        save.setOnClickListener {
            val map: MutableMap<String, Any> = HashMap()
            map["fullName"] = fullName.text.toString()
            map["age"] = age.text.toString()
            map["phoneNumber"] = phoneNumber.text.toString()
            map["status"] = status.text.toString()

            fStore.collection("User Profiles").document(userID).update(map)
            Toast.makeText(it.context, "Profile info updated!", Toast.LENGTH_SHORT).show()
            dialogPlus.dismiss()
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun addProfilePhoto() {
        val dialogPlus = DialogPlus.newDialog(context)
            .setContentHolder(ViewHolder(R.layout.profile_image_upload_dialog))
            .setGravity(Gravity.CENTER).create()

        myViewTwo = dialogPlus.holderView
        val imageViewUploadProfile: ImageView = myViewTwo.findViewById(R.id.imageViewUploadProfile)
        val browseBtnProfile: Button = myViewTwo.findViewById(R.id.browseBtnProfile)
        val uploadBtnProfile: Button = myViewTwo.findViewById(R.id.uploadBtnProfile)
        val closeBtnProfile: Button = myViewTwo.findViewById(R.id.closeBtnProfile)

        dialogPlus.show()

        closeBtnProfile.setOnClickListener {
            dialogPlus.dismiss()
        }

        browseBtnProfile.setOnClickListener {
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

        uploadBtnProfile.setOnClickListener {
            val progressDialog = ProgressDialog(context as Activity?)
            progressDialog.setTitle("Profile image uploader")
            progressDialog.show()

            val userID = auth.currentUser?.uid

            val randomKey : String = UUID.randomUUID().toString()
            val storage: FirebaseStorage = FirebaseStorage.getInstance()
            val uploader: StorageReference = storage.getReference("ProfilePics/${userID}/${randomKey}")

            uploader.putFile(filePath)
                .addOnSuccessListener {
                    //got uri
                    uploader.downloadUrl.addOnSuccessListener {
                        progressDialog.dismiss()
                        val imageURL : String = it.toString();

                        val map: MutableMap<String, Any> = HashMap()
                        map["profileimage"] = it.toString()

                        fStore.collection("User Profiles").document(userID!!).update(map)

                        imageViewUploadProfile.setImageResource(R.drawable.ic_baseline_file_upload_24)
                        //Glide.with(this).load(imageURL).into(user_profile_photo);
                        Toast.makeText(context as Activity?, "Profile image uploaded!", Toast.LENGTH_SHORT).show()
                        dialogPlus.dismiss()
                    }
                }
                .addOnProgressListener {
                    val percent = (100 * it.bytesTransferred) / it.totalByteCount
                    progressDialog.setMessage("Upload Percent : $percent%")
                }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK) {
            filePath = data?.data!!                              //image uri is in filepath
            try {
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(filePath)
                bitmap = BitmapFactory.decodeStream(inputStream)
                myViewTwo.imageViewUploadProfile.setImageBitmap(bitmap)              //setting image view with the selected image
            } catch (ex: Exception) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    //-------------------------------------------------------------------------------------------------------------------------
}