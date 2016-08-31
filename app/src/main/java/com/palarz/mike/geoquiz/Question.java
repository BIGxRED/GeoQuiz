package com.palarz.mike.geoquiz;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by QNP684 on 8/24/2016.
 */
public class Question implements Parcelable {

    private int mTextResID; //question text
    private boolean mAnswerTrue;    //answer of the question
    private boolean mIsCheater; //stored to check if the user cheated or not

    public Question(int textResID, boolean answerTrue, boolean isCheater){
        mTextResID = textResID;
        mAnswerTrue = answerTrue;
        mIsCheater = isCheater;
    }

    public Question(Parcel in){
        mTextResID = in.readInt();
        mAnswerTrue = in.readByte() != 0;   //Shorthand for if it is 1, set to true; false otherwise
        mIsCheater = in.readByte() != 0;
    }

    public int getTextResID() {
        return mTextResID;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public boolean getIsCheater(){
        return mIsCheater;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public void setTextResID(int textResID) {
        mTextResID = textResID;
    }

    public void setIsCheater(boolean isCheater){
        mIsCheater = isCheater;
    }

    public int describeContents(){  //don't know what this is for, but required for Parcelable
        return 0;
    }

    public void writeToParcel(Parcel out, int flags){
        out.writeInt(mTextResID);
        out.writeByte((byte) ( (mAnswerTrue) ? 1 : 0)); //no writeBoolean() function, really?
        out.writeByte((byte) ( (mIsCheater) ? 1 : 0));
    }

    //Additional code required to use Parcelable
    public static final Parcelable.Creator <Question> CREATOR
            = new Parcelable.Creator<Question>(){

        public Question createFromParcel(Parcel in){
            return new Question(in);
        }

        public Question [] newArray(int size){
            return new Question[size];
        }
    };
}
