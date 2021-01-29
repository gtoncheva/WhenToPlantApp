package com.searonix.whentoplantapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.searonix.whentoplantapp.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        var imgview = image_view2
        val storage = Firebase.storage

        // Create a storage reference from app
        val storageRef = storage.reference

        //Create a reference with an initial file path and name
        val pathReference = storageRef.child("apple-tree.jpg")
        pathReference.downloadUrl.addOnSuccessListener {Uri->
            val imageURL = Uri.toString()

            Glide.with(this)
                .load(imageURL)
                .fitCenter()
                .into(imgview)

        }

        db.collection("treeTypes")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val treenames = task.result!!.map { snapshot ->
                        snapshot["treeName"].toString()
                    }

                    val treeinfos = task.result!!.map { snapshot ->
                        snapshot["info"].toString()
                    }

                    Log.d("fragmentLog", "list of treenames: + $treenames")
                    Log.d("fragmentLog", "list of treeinfos: + $treeinfos")

                    //set apple info and name only for now
                    text_info.text = treeinfos[0]
                    text_home.text = treenames[0]
                    
                } else {
                    Log.w("fragmentLog", "Error getting documents.", task.exception)
                }
            }

    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}

