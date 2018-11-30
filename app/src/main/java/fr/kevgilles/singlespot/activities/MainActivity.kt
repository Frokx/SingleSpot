package fr.kevgilles.singlespot.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.kevgilles.singlespot.Constants
import fr.kevgilles.singlespot.R
import fr.kevgilles.singlespot.adapter.AreaAdapter
import fr.kevgilles.singlespot.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mMainViewModel: MainViewModel

    private lateinit var mAdapter: AreaAdapter
    private val mAreaList = Arrays.asList(
        "Area A", "Area B", "Area C", "Area D",
        "Area E", "Area F", "Area G", "Area H", "Area I", "Area J"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mMainViewModel.isReadyToWork().observe(this, Observer { nextBatch() })
        refreshView()

        button.setOnClickListener { mMainViewModel.getRemoteData() }
    }

    private fun nextBatch() {
        if (!mMainViewModel.isWorkOver() && mMainViewModel.isReadyToWork().value!!) {
            mMainViewModel.keepWorking()
        }
    }

    private fun refreshView() {
        mAdapter = AreaAdapter(mAreaList, this)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
    }

    override fun onClick(view: View) {
        if (view.tag != null) {
            val newIntent = Intent(this, LocationPointActivity::class.java)
            newIntent.putExtra(Constants.AREA_NAME, mAdapter.getItemAt(view.tag as Int))
            startActivity(newIntent)
        }
    }
}
