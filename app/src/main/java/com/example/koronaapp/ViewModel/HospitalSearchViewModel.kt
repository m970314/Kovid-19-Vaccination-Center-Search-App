package com.example.koronaapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.koronaapp.Model.HospitalInfoResponse
import com.example.koronaapp.const.Hospitalconst
import com.example.koronaapp.network.HospitalRepositori

class HospitalSearchViewModel(application: Application) : AndroidViewModel(application) {
    private var haspitalSearchRepository: HospitalRepositori
    private var haspitalInfoResponseLiveData: LiveData<HospitalInfoResponse>

    init {
        haspitalSearchRepository = HospitalRepositori()
        haspitalInfoResponseLiveData = haspitalSearchRepository.getHospitalResponseLiveData()
    }

    fun searchHospital() {
        for(i in 0 .. 10) {
            haspitalSearchRepository.searchhaspital(
                Hospitalconst.page.toString(),
                Hospitalconst.perPage.toString(),
                Hospitalconst.serviceKey
            )
            Hospitalconst.page = Hospitalconst.page + 1
        }
    }
}