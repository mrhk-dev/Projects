package com.rahmath888hussain.zomatoexpensetracker

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class DatesModelClass : BaseObservable() {

    var id:String = "0"
    @Bindable
    var date: String = "0"
    set(value) {
        field = value
        notifyPropertyChanged(BR.date)
    }
    @Bindable
    var profitLoss: String = "0"
        set(value) {
            field = value
            notifyPropertyChanged(BR.profitLoss)
        }
    var petrol: String = ""
    var expenses: String = ""
    var cashCollected: String = ""
    var tripCollected: String = ""
    var distanceTraveled: String = ""
}
