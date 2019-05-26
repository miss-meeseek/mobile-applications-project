package com.example.imagegallery


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.image_detail.view.*

class DetailFragment : Fragment() {
    fun showDetails(pos: Int, s: String) {
        if (dualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            var details = DrawFragment.newInstance(pos)        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

            // Execute a transaction, replacing any existing fragment
            // with this one inside the frame.
            fragmentManager?.beginTransaction()?.apply {
                if (pos == 0) {
                    this.replace(R.id.details, details);
                } else {
                    this.replace(R.id.details, details);
                }
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                commit()
                // }
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            val intent = Intent().apply {
                setClass(activity, DrawActivity::class.java)                // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                putExtra("index", pos)
            }
            startActivityForResult(intent, 3)
        }

    }
    val shownIndex: Int by lazy {
        arguments?.getInt("index", 0) ?: 0
    }

    private var dualPane: Boolean = false

    var curRating = 0.0f

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val detailsFrame: View? = activity?.findViewById(R.id.details)
        dualPane = detailsFrame?.visibility == View.VISIBLE

        curRating = savedInstanceState?.getFloat("curRating", 0.0f) ?: 0.0f
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat("curRating", curRating)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container == null) {
            return null
        }
        val view: View = inflater!!.inflate(R.layout.image_detail, container, false)

        view.draw.setOnClickListener { v ->
            showDetails(shownIndex, "draw")
        }

        view.save.setOnClickListener { v ->
            showDetails(shownIndex, "save")
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        val path = StorageManager.getPrivateAlbumStorageDir(context!!, "ImageGallery")?.path + "/" +  GalleryDatabase.getInstance(context!!).userDao().getFilename(shownIndex+1)
        Glide.with(this).load(path).placeholder(R.drawable.ic_launcher_foreground).into(img_det).getSize { w: Int, h: Int ->
            img_det.layoutParams.width = w
            img_det.layoutParams.height = h
        }
        */


    }

    companion object {
        fun newInstance(index: Int): DetailFragment {
            val f = DetailFragment()

            // Supply index input as an argument.
            val args = Bundle()
            args.putInt("index", index)
            f.arguments = args
            return f
        }
    }

}