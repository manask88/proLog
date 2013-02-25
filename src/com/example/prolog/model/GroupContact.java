package com.example.prolog.model;

public class GroupContact {
	long groupId;
	long contactId;
	
	public GroupContact () {
		groupId=0;
		contactId=0;
		
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getContactId() {
		return contactId;
	}

	public void setContactId(long contactId) {
		this.contactId = contactId;
	}
	
	
}
