/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\users\\F58004A\\git\\SeekBarTestGit\\SeekBarTest\\src\\com\\flaviocapaccio\\seekbartest\\ISeekBarService.aidl
 */
package com.flaviocapaccio.seekbartest;
public interface ISeekBarService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.flaviocapaccio.seekbartest.ISeekBarService
{
private static final java.lang.String DESCRIPTOR = "com.flaviocapaccio.seekbartest.ISeekBarService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.flaviocapaccio.seekbartest.ISeekBarService interface,
 * generating a proxy if needed.
 */
public static com.flaviocapaccio.seekbartest.ISeekBarService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.flaviocapaccio.seekbartest.ISeekBarService))) {
return ((com.flaviocapaccio.seekbartest.ISeekBarService)iin);
}
return new com.flaviocapaccio.seekbartest.ISeekBarService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_notifySettingCompleted:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _result = this.notifySettingCompleted(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.flaviocapaccio.seekbartest.ISeekBarService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.lang.String notifySettingCompleted(int progress) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(progress);
mRemote.transact(Stub.TRANSACTION_notifySettingCompleted, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_notifySettingCompleted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public java.lang.String notifySettingCompleted(int progress) throws android.os.RemoteException;
}
