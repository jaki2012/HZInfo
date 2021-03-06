// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.4.2
//
// <auto-generated>
//
// Generated from file `UserInfo.java'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.hnac.hzinfo.AMServer.base;

public class UserInfo implements java.lang.Cloneable, java.io.Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6681634458988369659L;

	public String UserAccount;

    public String UserName;

    public UserInfo()
    {
    }

    public UserInfo(String UserAccount, String UserName)
    {
        this.UserAccount = UserAccount;
        this.UserName = UserName;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        UserInfo _r = null;
        try
        {
            _r = (UserInfo)rhs;
        }
        catch(ClassCastException ex)
        {
        }

        if(_r != null)
        {
            if(UserAccount != _r.UserAccount)
            {
                if(UserAccount == null || _r.UserAccount == null || !UserAccount.equals(_r.UserAccount))
                {
                    return false;
                }
            }
            if(UserName != _r.UserName)
            {
                if(UserName == null || _r.UserName == null || !UserName.equals(_r.UserName))
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public int
    hashCode()
    {
        int __h = 0;
        if(UserAccount != null)
        {
            __h = 5 * __h + UserAccount.hashCode();
        }
        if(UserName != null)
        {
            __h = 5 * __h + UserName.hashCode();
        }
        return __h;
    }

    public java.lang.Object
    clone()
    {
        java.lang.Object o = null;
        try
        {
            o = super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return o;
    }

    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeString(UserAccount);
        __os.writeString(UserName);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        UserAccount = __is.readString();
        UserName = __is.readString();
    }
}
