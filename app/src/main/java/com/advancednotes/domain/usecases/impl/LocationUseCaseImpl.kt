package com.advancednotes.domain.usecases.impl

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import com.advancednotes.domain.models.CombinedLocationResult
import com.advancednotes.domain.models.MyLocation
import com.advancednotes.domain.models.isNotEmpty
import com.advancednotes.domain.usecases.LocationUseCase
import com.advancednotes.utils.logs.LogsHelper.logd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationUseCaseImpl @Inject constructor(
    private val context: Context
) : LocationUseCase {
    private val fusedLocation = MutableStateFlow(MyLocation())
    private val gpsLocation = MutableStateFlow(MyLocation())
    private val networkLocation = MutableStateFlow(MyLocation())
    private val combinedLocationResult =
        combine(
            fusedLocation,
            gpsLocation,
            networkLocation
        ) { fusedLocation, gpsLocation, networkLocation ->
            CombinedLocationResult(fusedLocation, gpsLocation, networkLocation)
        }

    private val locationManager: LocationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private var fusedLocationListener: LocationListener? = null
    private var gpsLocationListener: LocationListener? = null
    private var networkLocationListener: LocationListener? = null

    @SuppressLint("MissingPermission")
    override suspend fun getLocation(onLocationReceived: (myLocation: MyLocation) -> Unit) {
        try {
            val hasFused: Boolean =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    locationManager.isProviderEnabled(LocationManager.FUSED_PROVIDER)
                } else false
            val hasGps: Boolean =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val hasNetwork: Boolean =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (hasFused) {
                withContext(Dispatchers.Main) {
                    fusedLocationListener = object : LocationListener {
                        override fun onLocationChanged(location: Location) {
                            fusedLocation.tryEmit(
                                MyLocation(
                                    latitude = location.latitude,
                                    longitude = location.longitude,
                                    accuracy = location.accuracy
                                )
                            )
                        }

                        @Deprecated("Deprecated in Java")
                        override fun onStatusChanged(
                            provider: String,
                            status: Int,
                            extras: Bundle
                        ) {
                        }

                        override fun onProviderEnabled(provider: String) {}
                        override fun onProviderDisabled(provider: String) {}
                    }

                    locationManager.requestLocationUpdates(
                        LocationManager.FUSED_PROVIDER,
                        1000,
                        0f,
                        fusedLocationListener!!
                    )
                }
            } else {
                if (hasGps) {
                    withContext(Dispatchers.Main) {
                        gpsLocationListener = object : LocationListener {
                            override fun onLocationChanged(location: Location) {
                                gpsLocation.tryEmit(
                                    MyLocation(
                                        latitude = location.latitude,
                                        longitude = location.longitude,
                                        accuracy = location.accuracy
                                    )
                                )
                            }

                            @Deprecated("Deprecated in Java")
                            override fun onStatusChanged(
                                provider: String,
                                status: Int,
                                extras: Bundle
                            ) {
                            }

                            override fun onProviderEnabled(provider: String) {}
                            override fun onProviderDisabled(provider: String) {}
                        }

                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            1000,
                            0f,
                            gpsLocationListener!!
                        )
                    }
                }

                if (hasNetwork) {
                    withContext(Dispatchers.Main) {
                        networkLocationListener = object : LocationListener {
                            override fun onLocationChanged(location: Location) {
                                networkLocation.tryEmit(
                                    MyLocation(
                                        latitude = location.latitude,
                                        longitude = location.longitude,
                                        accuracy = location.accuracy
                                    )
                                )
                            }

                            @Deprecated("Deprecated in Java")
                            override fun onStatusChanged(
                                provider: String,
                                status: Int,
                                extras: Bundle
                            ) {
                            }

                            override fun onProviderEnabled(provider: String) {}
                            override fun onProviderDisabled(provider: String) {}
                        }

                        locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1000,
                            0f,
                            networkLocationListener!!
                        )
                    }
                }
            }

            logd("hasFused $hasFused")
            logd("hasGps $hasGps")
            logd("hasNetwork $hasNetwork")

            combinedLocationResult.collectLatest { combinedLocationResult ->
                logd("combinedLocationResult $combinedLocationResult")

                val betterLocation: MyLocation = if (hasFused) {
                    combinedLocationResult.fusedLocation
                } else if (hasGps) {
                    if (hasNetwork) {
                        if (combinedLocationResult.gpsLocation.accuracy > combinedLocationResult.networkLocation.accuracy) {
                            combinedLocationResult.gpsLocation
                        } else {
                            combinedLocationResult.networkLocation
                        }
                    } else {
                        combinedLocationResult.gpsLocation
                    }
                } else {
                    combinedLocationResult.networkLocation
                }

                if (betterLocation.isNotEmpty()) {
                    onLocationReceived(betterLocation)
                    stopLocationUpdates()
                }
            }
        } catch (e: Exception) {
            logd("exception $e")
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationListener?.let { locationManager.removeUpdates(it) }
        gpsLocationListener?.let { locationManager.removeUpdates(it) }
        networkLocationListener?.let { locationManager.removeUpdates(it) }
    }
}