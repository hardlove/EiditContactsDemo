package com.globalroam.editcontactsdemo;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public  class Contact {
	public static final class Phone {
		public static final int TYPE_ASSISTANT = 19;
        public static final int TYPE_CALLBACK = 8;
        public static final int TYPE_CAR = 9;
        public static final int TYPE_COMPANY_MAIN = 10;
        public static final int TYPE_FAX_HOME = 5;
        public static final int TYPE_FAX_WORK = 4;
        public static final int TYPE_HOME = 1;
        public static final int TYPE_ISDN = 11;
        public static final int TYPE_MAIN = 12;
        public static final int TYPE_MMS = 20;
        public static final int TYPE_MOBILE = 2;
        public static final int TYPE_OTHER = 7;
        public static final int TYPE_OTHER_FAX = 13;
        public static final int TYPE_PAGER = 6;
        public static final int TYPE_RADIO = 14;
        public static final int TYPE_TELEX = 15;
        public static final int TYPE_TTY_TDD = 16;
        public static final int TYPE_WORK = 3;
        public static final int TYPE_WORK_MOBILE = 17;
        public static final int TYPE_WORK_PAGER = 18;
		private String name;
		private String number;

		private int type;

		public Phone(String number) {
			this(number,TYPE_OTHER);
		}

		public Phone(String number, int type) {
			this(null,number,type);
		}
		public Phone(String name,String number, int type) {
			super();
			if(TextUtils.isEmpty(number)){
				throw new NullPointerException("the number is empty.");
			}
			if(TextUtils.isEmpty(name)){
				name = "Unknow";
			}
			
			this.number = number;
			this.type = type;
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public String getNumber() {
			return number;
		}

		public int getType() {
			return type;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setNumber(String number) {
			this.number = number;
		}


		public void setType(int type) {
			this.type = type;
		}


		@Override
		public String toString() {
			return "[name:"+name+"]number:" + this.number + "[type:" + this.type + "]";
		}
		@Override
		public boolean equals(Object o) {
			// TODO Auto-generated method stub
			if(this == o){
				return true;
			}
			
			if(o instanceof Phone){
				Phone p = (Phone)o;
				if(this.getType() != p.getType()){
					return false;
				}
				if(!this.getName().equals(p.getName())){
					return false;
				}
				if(!this.getNumber().equals(p.getNumber())){
					return false;
				}
				return true;
			}
			
			return super.equals(o);
		}

	}
	private int age;

	private int contactId;

	private String email;

	private String name;

	private List<Phone> phones;

	private String photoId;

	private char sex;

	public Contact(String name) {
		this(name,null);
	}

	public Contact(String name, List<Phone> phones) {
		super();
		if (TextUtils.isEmpty(name)) {
			name = "Unknow";
		}
		this.name = name;

		if (phones == null) {
			return;
		}
		if (this.phones == null) {
			this.phones = new ArrayList<Phone>();
		} else {
			this.phones.clear();
		}
		this.phones.addAll(phones);

	}

	public boolean addPhone(Phone phone) {
		if (phone == null) {
			return false;
		}
		
		try {
			this.phones.add(phone);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public int getAge() {
		return age;
	}

	public int getContactId() {
		return contactId;
	}

	public String getEmail() {
		return email;
	}


	public String getName() {
		return name;
	}
	public List<Phone> getPhones() {
		return phones;
	}
	public String getPhotoId() {
		return photoId;
	}
	public char getSex() {
		return sex;
	}
	public boolean removePhone(Phone phone) {
		if (phone == null) {
			return false;
		}
		
		if(this.phones.contains(phone)){
			try {
				this.phones.remove(phone);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	public boolean removePhone(String number) {
		if (TextUtils.isEmpty(number)) {
			return false;
		}
		
		Iterator<Phone> iterator = this.phones.iterator();
		while (iterator.hasNext()) {
			if(number.equals(iterator.next().number)){
				try {
					this.phones.remove(iterator.next());
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}
	

	public void setSex(char sex) {
		this.sex = sex;
	}

}
