package com.example.imagegallery

import android .content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.image_list.*

public class ListFragment : Fragment() {
    private var dualPane: Boolean = false

    private lateinit var viewManager: RecyclerView.LayoutManager
    private var curCheckPosition = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        curCheckPosition = savedInstanceState?.getInt("curChoice", 0) ?: 0
        val detailsFrame: View? = activity?.findViewById(R.id.details)
        dualPane = detailsFrame?.visibility == View.VISIBLE
        Log.d("DUAL", dualPane.toString())
        if (dualPane) {
            showDetails(curCheckPosition)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 3) {
            notifyDataSetChanged()
            Log.d("ABDD", "ABDD")
        }
    }

    fun showDetails(pos: Int) {
        if (dualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
<<<<<<< HEAD
             var details = DetailFragment.newInstance(pos)
=======
             var details = DrawFragment.newInstance(pos)        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
>>>>>>> de38ca717af24345259d541be5b6b68312f3f30a

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                fragmentManager?.beginTransaction()?.apply {
                    if (pos == 0) {
<<<<<<< HEAD
                        this.replace(R.id.details, details)
                    } else {
                        this.replace(R.id.details, details)
=======
                        this.replace(R.id.details, details);
                    } else {
                        this.replace(R.id.details, details);
>>>>>>> de38ca717af24345259d541be5b6b68312f3f30a
                    }
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    commit()
               // }
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            val intent = Intent().apply {
<<<<<<< HEAD
                setClass(activity, DetailActivity::class.java)
=======
                setClass(activity, DrawActivity::class.java)                // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
>>>>>>> de38ca717af24345259d541be5b6b68312f3f30a
                putExtra("index", pos)
            }
            startActivityForResult(intent, 3)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.image_list, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("IMGd LIST", recycler_view.toString())


        viewManager = GridLayoutManager(activity, 2)
        recycler_view.layoutManager = viewManager
        recycler_view.adapter = RecyclerAdapter(context)
        recycler_view.addOnItemTouchListener(
            RecyclerItemClickListener(view.context, recycler_view, object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    showDetails(position)
                }

                override fun onLongItemClick(view: View?, position: Int) {
                    // do whatever
                }
            })
        )
    }

 /*   fun sort() {
        list.sortWith(Comparator { o1, o2 ->
            when {
                o1.second < o2.second -> 1
                o1.second == o2.second -> 0
                else -> -1
            }
        })
    }
*/
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("curChoice", curCheckPosition)
    }



    fun notifyDataSetChanged() {
        recycler_view.adapter?.notifyDataSetChanged()
    }

    fun notifyDataInserted() {
        recycler_view.adapter?.notifyItemInserted(id)
    }

    fun notifyDataSetUpdated(i: Int) {
        recycler_view.adapter?.notifyDataSetChanged()
    }
}