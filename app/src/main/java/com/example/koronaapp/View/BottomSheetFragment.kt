package com.example.koronaapp.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.koronaapp.R
import com.example.koronaapp.databinding.FragmentBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment() : BottomSheetDialogFragment(){
    private var _binding: FragmentBottomsheetBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val address = arguments?.getString("address")
        val centerName = arguments?.getString("centerName")
        val facilityName = arguments?.getString("facilityName")
        val phoneNumber = arguments?.getString("phoneNumber")
        val updatedAt = arguments?.getString("updatedAt")
        view?.findViewById<TextView>(R.id.centerName)?.setText(centerName)
        view?.findViewById<TextView>(R.id.address)?.setText(address)
        view?.findViewById<TextView>(R.id.facilityName)?.setText(facilityName)
        view?.findViewById<TextView>(R.id.phoneNumber)?.setText(phoneNumber)
        view?.findViewById<TextView>(R.id.updatedAt)?.setText(updatedAt)
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from<View>(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        // 드래그해도 팝업이 종료되지 않도록
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }
}