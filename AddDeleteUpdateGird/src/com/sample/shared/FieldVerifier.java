package com.sample.shared;



public class FieldVerifier {
	public static boolean validateInputs(String fname, String lname,
			String phone, String email) {
		
		if((fname==null) && (lname ==null) && (phone ==null)&& (email ==null)){
			return false;
		}return true;
	}

}
