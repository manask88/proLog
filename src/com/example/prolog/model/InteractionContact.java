package com.example.prolog.model;

public class InteractionContact {
	long interactionId;
	long contactId;
	
	public InteractionContact () {
		interactionId=0;
		contactId=0;
		
	}

	public InteractionContact (long contactId) {
		interactionId=0;
		this.contactId=contactId;
		
	}


	public long getInteractionId() {
		return interactionId;
	}



	public void setInteractionId(long interactionId) {
		this.interactionId = interactionId;
	}



	public long getContactId() {
		return contactId;
	}

	public void setContactId(long contactId) {
		this.contactId = contactId;
	}
	
	
}
