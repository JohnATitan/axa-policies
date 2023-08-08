package mx.axa.insurance_policy_list.data.model

import android.os.Parcel
import android.os.Parcelable

data class Policy(
    val policy: String,
    val clause: String,
    val type: String,
    val insuredName: String,
    val insuredAmount: String,
    val servicePhone: String,
    val coinsurance: String,
    val detail: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(policy)
        parcel.writeString(clause)
        parcel.writeString(type)
        parcel.writeString(insuredName)
        parcel.writeString(insuredAmount)
        parcel.writeString(servicePhone)
        parcel.writeString(coinsurance)
        parcel.writeString(detail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Policy> {
        override fun createFromParcel(parcel: Parcel): Policy {
            return Policy(parcel)
        }

        override fun newArray(size: Int): Array<Policy?> {
            return arrayOfNulls(size)
        }
    }
}
