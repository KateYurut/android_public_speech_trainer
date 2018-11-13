package com.example.company.myapplication.views

import android.annotation.SuppressLint
import android.support.v4.app.FragmentManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import com.example.putkovdimi.trainspeech.DBTables.PresentationData
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.presentation_startpage_row.view.*
import java.util.*
import java.util.concurrent.TimeUnit
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AlertDialog
import android.util.Log
import com.example.company.myapplication.*
import com.example.putkovdimi.trainspeech.DBTables.SpeechDataBase


class PresentationStartpageRow(private val presentation: PresentationData,private val firstPageBitmap: Bitmap?, private val ctx: Context): Item<ViewHolder>() {
    companion object {
        const val activatedChangePresentationFlag = 1
    }

    var presentationId: Int? = null

    override fun getLayout(): Int {
        return R.layout.presentation_startpage_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.name_presentation_start_page_row.text = presentation.name
        viewHolder.itemView.time_limit_presentation_start_page_row.text = getStringPresentationTimeLimit(presentation.timeLimit!!)
        viewHolder.itemView.page_count_presentation_start_page_row.text = getStringPresentationPageCount(presentation.pageCount)
        viewHolder.itemView.image_view_presentation_start_page_row.setImageBitmap(firstPageBitmap)

        presentationId = presentation.id

        viewHolder.itemView.change_btn_presentation_start_page_row.setOnClickListener {
            val builder = AlertDialog.Builder(ctx)
            builder.setMessage(ctx.getString(R.string.change_presentation_task) + "${presentation.name} ?")

            builder.setPositiveButton(ctx.getString(R.string.change)) { _, _ ->
                val i = Intent(ctx, EditPresentationActivity::class.java)
                i.putExtra(ctx.getString(R.string.CURRENT_PRESENTATION_ID),presentation.id)
                i.putExtra(ctx.getString(R.string.changePresentationFlag), activatedChangePresentationFlag)
                startActivity(ctx,i,null)
            }

            builder.setNegativeButton(ctx.getString(R.string.no)) { _, _ -> }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        viewHolder.itemView.rm_presentation_start_page_row.setOnClickListener {

            val builder = AlertDialog.Builder(ctx)
            builder.setMessage(ctx.getString(R.string.request_for_remove_presentation) + "${presentation.name} ?")
            builder.setPositiveButton(ctx.getString(R.string.remove)) { _, _ ->
                val position = StartPageActivity.adapter?.getAdapterPosition(this)
                StartPageActivity.adapter?.remove(this)
                StartPageActivity.adapter?.notifyItemRemoved(position!!)

                if (presentationId != null)
                    SpeechDataBase.getInstance(ctx)?.PresentationDataDao()?.deletePresentationWithId(presentationId!!)
                else {
                    Log.d("presentation_row_test", "error id = $presentationId")
                }
            }

            builder.setNegativeButton(ctx.getString(R.string.leave)) { _, _ ->

            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    @SuppressLint("UseSparseArrays")
    private fun getStringPresentationTimeLimit(t: Long?): String {
        if (t == null)
            return "undefined"

        var millisUntilFinishedVar: Long = t


        val minutes = TimeUnit.SECONDS.toMinutes(millisUntilFinishedVar)
        millisUntilFinishedVar -= TimeUnit.MINUTES.toSeconds(minutes)

        val seconds = millisUntilFinishedVar

        return String.format(
                Locale.getDefault(),
                "%02d min: %02d sec",
                minutes, seconds
        )
    }

    private fun getStringPresentationPageCount(n: Int?): String {
        if (n == null || n <= 0) {
            return "undefined"
        }

        val titles = arrayOf("$n слайд","$n слайда","$n слайдов")
        val cases = arrayOf(2, 0, 1, 1, 1, 2)

        return titles[if (n % 100 in 5..19) 2 else cases[if (n % 10 < 5) n % 10 else 5]]
    }
}