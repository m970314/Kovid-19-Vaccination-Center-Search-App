package com.example.koronaapp.View
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.koronaapp.Model.HospitalInfoResponse
import com.example.koronaapp.R
import com.example.koronaapp.databinding.FragmentMapBinding
import com.example.koronaapp.ViewModel.HospitalSearchViewModel
import com.example.koronaapp.network.AppDatabase
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.google.android.gms.location.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MapViewFragment : Fragment(), OnMapReadyCallback, Overlay.OnClickListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private lateinit var db : AppDatabase
    var count : Int = 0
    var pnum : Int = 0
    var cnum : Int = 0
    var adr: String = "123123"
    var centerName: String = "123123"
    var facilityName: String = "123123"
    var phoneNumber: String = "123123"
    var updatedAt: String = "123123"
    var centertype: String = "123123"
    private var lat: Double = 37.33
    lateinit var currentLocation: Location
    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private var lng: Double = 126.58
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HospitalSearchViewModel
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    lateinit var hospitalList : List<HospitalInfoResponse>
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gson = AppDatabase.DatabaseModule.provideGson()
        db = AppDatabase.DatabaseModule.provideDatabase(requireContext(),gson)
        viewModel = ViewModelProvider(this).get(HospitalSearchViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mLocationRequest = LocationRequest.create().apply {
            interval = 2000 // 업데이트 간격 단위(밀리초)
            fastestInterval = 1000 // 가장 빠른 업데이트 간격 단위(밀리초)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 정확성
            maxWaitTime = 2000 // 위치 갱신 요청 최대 대기 시간 (밀리초)
        }
        CoroutineScope(Dispatchers.Main).launch {
            val load = async(Dispatchers.IO) {
                hospitalList = db.roomDao().getAll()
            }
            load.await()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.naver_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.naver_map, it).commit()
            }
        mapFragment.getMapAsync(this)
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        viewModel.searchHospital()
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        for (i in 0 .. 10) {
            if(i >= 10){
                break
            }
            for (j in 0 .. 10) {
                if(j >= 10){
                    continue
                }
                Log.d("asdf",count.toString())
                lat = hospitalList.get(i).itemModelList.get(j).latitude.toDouble()
                lng = hospitalList.get(i).itemModelList.get(j).longitude.toDouble()
                centertype = hospitalList.get(i).itemModelList.get(j).centerType
                Markerset(count, lat, lng, centertype)
                count++
            }
        }
        view?.findViewById<ImageButton>(R.id.fab_tracking)?.setOnClickListener {
            if (checkPermissionForLocation(requireContext())) {
                startLocationUpdates()
            }
        }
    }
    private fun Markerset(i: Int, lat: Double, lng: Double, centertype: String) {
        val marker = Marker()
        marker.position = LatLng(
            lat,
            lng
        )
        when (centertype) {
            "중앙/권역" -> marker.icon = MarkerIcons.RED
            "지역" -> marker.icon = MarkerIcons.YELLOW
        }
        marker.map = naverMap
        marker.zIndex = i
        marker.onClickListener = this
        marker.tag = i
    }

    override fun onClick(p0: Overlay): Boolean {
        pnum = p0.tag.toString().toInt() / 10
        cnum = p0.tag.toString().toInt() % 10
        for (i in 0 .. 11) {
            if(i >= 11){
                break
            }
            for (j in 0 .. 11) {
                if(j >= 11){
                    continue
                }
                lat = hospitalList.get(pnum).itemModelList.get(cnum).latitude.toDouble()
                lng = hospitalList.get(pnum).itemModelList.get(cnum).longitude.toDouble()
                centerName = hospitalList.get(pnum).itemModelList.get(cnum).centerName
                adr = hospitalList.get(pnum).itemModelList.get(cnum).address
                facilityName = hospitalList.get(pnum).itemModelList.get(cnum).facilityName
                phoneNumber = hospitalList.get(pnum).itemModelList.get(cnum).phoneNumber
                updatedAt = hospitalList.get(pnum).itemModelList.get(cnum).updatedAt
            }
        }
        val cameraPosition = CameraPosition(
            LatLng(
                lat,
                lng
            ), 12.1
        )
        naverMap.cameraPosition = cameraPosition
        val mbundle = Bundle()
        mbundle.putString("address", adr)
        mbundle.putString("centerName", centerName)
        mbundle.putString("facilityName", facilityName)
        mbundle.putString("phoneNumber", phoneNumber)
        mbundle.putString("updatedAt", updatedAt)
        val bottomsheet: BottomSheetDialogFragment = BottomSheetFragment()
        bottomsheet.arguments = mbundle
        bottomsheet.show(activity?.supportFragmentManager!!, bottomsheet.tag)
        return true
    }

    protected fun startLocationUpdates() {
        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청합니다.
        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }
    fun onLocationChanged(location: Location) {
        currentLocation = location
        naverMap.locationOverlay.run {
            isVisible = true
            position = LatLng(currentLocation.latitude,currentLocation.longitude)
        }
        view?.findViewById<ImageButton>(R.id.fab_tracking)?.setOnClickListener {
            val cameraPosition = CameraPosition(
                LatLng(
                    currentLocation.latitude,
                    currentLocation.longitude
                ), 12.1
            )
            naverMap.cameraPosition = cameraPosition
        }
    }
    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
                false
            }
        } else {
            true
        }
    }

    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {

            }
        }
    }
}