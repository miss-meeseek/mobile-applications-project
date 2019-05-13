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
    var list: ArrayList<Pair<Int, Float>> = ArrayList()

    private lateinit var viewManager: RecyclerView.LayoutManager
    private var curCheckPosition = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        curCheckPosition = savedInstanceState?.getInt("curChoice", 0) ?: 0
        val detailsFrame: View? = activity?.findViewById(R.id.details)
        dualPane = detailsFrame?.visibility == View.VISIBLE
        Log.d("DUAL", dualPane.toString())
        if (dualPane) {
            showDetails(curCheckPosition, list)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 3) {
            notifyDataSetChanged()
            Log.d("ABDD", "ABDD")
        }
    }

    fun showDetails(pos: Int, list: ArrayList<Pair<Int, Float>>) {
        val position = list[pos].first
        curCheckPosition = position

        if (dualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
             var details = DetailFragment.newInstance(position)

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                fragmentManager?.beginTransaction()?.apply {
                    if (position == 0) {
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
                setClass(activity, DetailActivity::class.java)
                putExtra("index", position)
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

        recreate()

        viewManager = GridLayoutManager(activity, 2)
        recycler_view.layoutManager = viewManager
        recycler_view.adapter = RecyclerAdapter(list, context)
        recycler_view.addOnItemTouchListener(
            RecyclerItemClickListener(view.context, recycler_view, object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    Log.d("ABCD ", list[position].first.toString())
                    showDetails(position, list)
                }

                override fun onLongItemClick(view: View?, position: Int) {
                    // do whatever
                }
            })
        )
    }

    fun sort() {
        list.sortWith(Comparator { o1, o2 ->
            when {
                o1.second < o2.second -> 1
                o1.second == o2.second -> 0
                else -> -1
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("curChoice", curCheckPosition)
    }

    fun recreate() {
        val count = GalleryDatabase.getInstance(context!!).userDao().count()
        list.clear()
        for (i in 0..count) {
            val rating = GalleryDatabase.getInstance(context!!).userDao().getRatings(i+1)
            val no = GalleryDatabase.getInstance(context!!).userDao().getNoRatings(i+1)
            var pair: Pair<Int, Float>
            pair = if (no != 0) {
                Pair(i, rating / no)
            } else {
                Pair(i, 0.0f)
            }
            list.add(pair)
        }
        sort()
    }

    fun notifyDataSetChanged() {
        sort()
        recycler_view.adapter?.notifyDataSetChanged()
    }

    fun notifyDataInserted() {
        sort()
        val id = recycler_view.adapter?.itemCount!!
        list.add(Pair(id, 0.0f))
        recycler_view.adapter?.notifyItemInserted(id)
    }

    fun notifyDataSetUpdated(i: Int) {
        val rating = GalleryDatabase.getInstance(context!!).userDao().getRatings(i+1)
        val no = GalleryDatabase.getInstance(context!!).userDao().getNoRatings(i+1)

        val a = list.filter{ it.first == i }.single()
        list[list.indexOf(a)] = Pair(i, rating / no)
        sort()
        recycler_view.adapter?.notifyDataSetChanged()
    }
}