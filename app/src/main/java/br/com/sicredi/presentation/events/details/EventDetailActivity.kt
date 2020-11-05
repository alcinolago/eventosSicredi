package br.com.sicredi.presentation.events.details

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.sicredi.Constants
import br.com.sicredi.R
import br.com.sicredi.core.extension.setSafeOnClickListener
import br.com.sicredi.core.util.DialogProgressBar
import br.com.sicredi.core.util.InputTextWatcher
import br.com.sicredi.core.util.Util.getDateTime
import br.com.sicredi.data.model.events.EventsResponse
import br.com.sicredi.databinding.ActivityEventDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventDetailActivity : AppCompatActivity() {

    private val eventDetailViewModel: EventDetailViewModel by viewModel()
    private lateinit var eventBehaviorBottomSheet: BottomSheetBehavior<*>
    private var eventId = 0
    private lateinit var shareDescription: String
    private lateinit var shareDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityEventDetailBinding>(this, R.layout.activity_event_detail)
        binding.viewModel = eventDetailViewModel
        binding.lifecycleOwner = this

        initViews()
        getIntentData()
        observeViewModel()
        observerValidateDataEntry()
        setupToolbar(Constants.TOOLBAR_EVENT_DETAIL_TITLE)
    }

    private fun initViews() {
        shareEvent.setSafeOnClickListener { share() }
        editTextName!!.addTextChangedListener(InputTextWatcher(input_layout_name!!))
        editTextEmail!!.addTextChangedListener(InputTextWatcher(input_layout_email!!))

        eventBehaviorBottomSheet = BottomSheetBehavior.from(eventBottomSheetDetail)

        eventBehaviorBottomSheet.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {}

                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onStateChanged(p0: View, state: Int) {
                    when (state) {
                        BottomSheetBehavior.STATE_EXPANDED ->
                            expandTotalArrow.setImageDrawable(getDrawable(R.drawable.ic_arrow_drop_down_black_24dp))
                        else ->
                            expandTotalArrow.setImageDrawable(getDrawable(R.drawable.ic_arrow_drop_up_white_24dp))
                    }
                }
            }
        )

        bottomSheetButton.setSafeOnClickListener {
            eventBehaviorBottomSheet.state =
                if (eventBehaviorBottomSheet.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    BottomSheetBehavior.STATE_EXPANDED
                } else {
                    BottomSheetBehavior.STATE_COLLAPSED
                }
        }
    }

    private fun setupToolbar(title: String) {

        setSupportActionBar(toolbar_detail)
        supportActionBar?.title = title
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getIntentData() {
        intent.getIntExtra(Constants.EVENT_DETAIL, 0).let {
            eventId = it
            getEventById()
        }

    }

    private fun getEventById() {
        eventDetailViewModel.getEventById(eventId)
    }

    private fun share() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Participe da $shareDescription vai ser no dia $shareDate"
        )
        startActivity(
            Intent.createChooser(
                sharingIntent,
                resources.getString(R.string.share_title)
            )
        )
    }

    private fun observeViewModel() {

        eventDetailViewModel.eventMutableLiveData.observe(this, { eventResponse ->
            populateViewDetails(eventResponse)
        })

        eventDetailViewModel.isLoading.observe(this, {
            it?.let { loading ->
                if (loading) DialogProgressBar.show(this) else DialogProgressBar.dismiss()
            }
        })

        eventDetailViewModel.eventCheckIMessage.observe(this, { message ->
            eventBehaviorBottomSheet.state =
                if (eventBehaviorBottomSheet.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    BottomSheetBehavior.STATE_EXPANDED
                } else {
                    BottomSheetBehavior.STATE_COLLAPSED
                }

            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        })
    }

    private fun observerValidateDataEntry() {

        eventDetailViewModel.nameErrorMessage.observe(this, {
            it?.getContentIfNotHandled()?.let { errorMessage ->
                input_layout_name.error = errorMessage
                editTextName.requestFocus()
            }
        })

        eventDetailViewModel.emailErrorMessage.observe(this, {
            it?.getContentIfNotHandled()?.let { errorMessage ->
                input_layout_email.error = errorMessage
                editTextEmail.requestFocus()
            }
        })
    }

    private fun populateViewDetails(eventResponse: EventsResponse) {
        Picasso.get()
            .load(eventResponse.image)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(eventImage)
        shareDescription = eventResponse.title!!
        shareDate = getDateTime(eventResponse.date!!)
        eventTitle.text = eventResponse.title
        textViewToolbarEventTitle.text = eventResponse.title
        eventDescription.text = eventResponse.description
        eventDate.text = getDateTime(eventResponse.date!!)
    }
}
