package com.example.llotis.fragintheair;

import android.os.Parcel;
import android.os.Parcelable;

public class FlightObject implements Parcelable {
    String departsAt;
    String arrivesAt;

    String originAirport;
    String originTerminal;

    String destinationAirport;
    String destinationMarketingAirline; //Γερο:στο JSON Array τα marketing_airline, operating_airline, flight_number και aircraft δεν είναι κάτω απο το destination
    String destinationOperatingAirline;
    String destinationFlightNumber;
    String destinationAircraft;

    String bookingInfoTravelClass;
    String bookingInfoBookingCode;
    String bookingInfoSeatsRemaining;

    String totalPrice;

    String adultTotalFare;
    String adultTax;

    Boolean refundableBoolean;
    Boolean changePenaltiesBoolean;

    String currency;

    public FlightObject() {
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDepartsAt() {
        return departsAt;
    }

    public void setDepartsAt(String departsAt) {
        this.departsAt = departsAt;
    }

    public String getArrivesAt() {
        return arrivesAt;
    }

    public void setArrivesAt(String arrivesAt) {
        this.arrivesAt = arrivesAt;
    }

    public String getOriginAirport() {
        return originAirport;
    }

    public void setOriginAirport(String originAirport) {
        this.originAirport = originAirport;
    }

    public String getOriginTerminal() {
        return originTerminal;
    }

    public String getDestinationAirport() { return destinationAirport; }

    public void setDestinationAirport(String destinationAirport) { this.destinationAirport = destinationAirport; }

    public void setOriginTerminal(String originTerminal) {
        this.originTerminal = originTerminal;
    }

    public String getDestinationMarketingAirline() {
        return destinationMarketingAirline;
    }

    public void setDestinationMarketingAirline(String destinationMarketingAirline) {
        this.destinationMarketingAirline = destinationMarketingAirline;
    }

    public String getDestinationOperatingAirline() {
        return destinationOperatingAirline;
    }

    public void setDestinationOperatingAirline(String destinationOperatingAirline) {
        this.destinationOperatingAirline = destinationOperatingAirline;
    }

    public String getDestinationFlightNumber() {
        return destinationFlightNumber;
    }

    public void setDestinationFlightNumber(String destinationFlightNumber) {
        this.destinationFlightNumber = destinationFlightNumber;
    }

    public String getDestinationAircraft() {
        return destinationAircraft;
    }

    public void setDestinationAircraft(String destinationAircraft) {
        this.destinationAircraft = destinationAircraft;
    }

    public String getBookingInfoTravelClass() {
        return bookingInfoTravelClass;
    }

    public void setBookingInfoTravelClass(String bookingInfoTravelClass) {
        this.bookingInfoTravelClass = bookingInfoTravelClass;
    }

    public String getBookingInfoBookingCode() {
        return bookingInfoBookingCode;
    }

    public void setBookingInfoBookingCode(String bookingInfoBookingCode) {
        this.bookingInfoBookingCode = bookingInfoBookingCode;
    }

    public String getBookingInfoSeatsRemaining() {
        return bookingInfoSeatsRemaining;
    }

    public void setBookingInfoSeatsRemaining(String bookingInfoSeatsRemaining) {
        this.bookingInfoSeatsRemaining = bookingInfoSeatsRemaining;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAdultTotalFare() {
        return adultTotalFare;
    }

    public void setAdultTotalFare(String adultTotalFare) {
        this.adultTotalFare = adultTotalFare;
    }

    public String getAdultTax() {
        return adultTax;
    }

    public void setAdultTax(String adultTax) {
        this.adultTax = adultTax;
    }

    public Boolean getRefundableBoolean() {
        return refundableBoolean;
    }

    public void setRefundableBoolean(Boolean refundableBoolean) {
        this.refundableBoolean = refundableBoolean;
    }

    public Boolean getChangePenaltiesBoolean() {
        return changePenaltiesBoolean;
    }

    public void setChangePenaltiesBoolean(Boolean changePenaltiesBoolean) {
        this.changePenaltiesBoolean = changePenaltiesBoolean;
    }

    protected FlightObject(Parcel in) {
        departsAt = in.readString();
        arrivesAt = in.readString();
        originAirport = in.readString();
        originTerminal = in.readString();
        destinationAirport = in.readString();
        destinationMarketingAirline = in.readString();
        destinationOperatingAirline = in.readString();
        destinationFlightNumber = in.readString();
        destinationAircraft = in.readString();
        bookingInfoTravelClass = in.readString();
        bookingInfoBookingCode = in.readString();
        bookingInfoSeatsRemaining = in.readString();
        totalPrice = in.readString();
        adultTotalFare = in.readString();
        adultTax = in.readString();
        byte refundableBooleanVal = in.readByte();
        refundableBoolean = refundableBooleanVal == 0x02 ? null : refundableBooleanVal != 0x00;
        byte changePenaltiesBooleanVal = in.readByte();
        changePenaltiesBoolean = changePenaltiesBooleanVal == 0x02 ? null : changePenaltiesBooleanVal != 0x00;
        currency = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(departsAt);
        dest.writeString(arrivesAt);
        dest.writeString(originAirport);
        dest.writeString(originTerminal);
        dest.writeString(destinationAirport);
        dest.writeString(destinationMarketingAirline);
        dest.writeString(destinationOperatingAirline);
        dest.writeString(destinationFlightNumber);
        dest.writeString(destinationAircraft);
        dest.writeString(bookingInfoTravelClass);
        dest.writeString(bookingInfoBookingCode);
        dest.writeString(bookingInfoSeatsRemaining);
        dest.writeString(totalPrice);
        dest.writeString(adultTotalFare);
        dest.writeString(adultTax);
        if (refundableBoolean == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (refundableBoolean ? 0x01 : 0x00));
        }
        if (changePenaltiesBoolean == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (changePenaltiesBoolean ? 0x01 : 0x00));
        }
        dest.writeString(currency);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FlightObject> CREATOR = new Parcelable.Creator<FlightObject>() {
        @Override
        public FlightObject createFromParcel(Parcel in) {
            return new FlightObject(in);
        }

        @Override
        public FlightObject[] newArray(int size) {
            return new FlightObject[size];
        }
    };
}