package data_off;

public class OFFStats {


	public static int TOTAL_PRODUCTS;
	public static int PRODUCTS_NOT_FOUND_THROUGH_API;
	public static int IMAGES_NOT_FOUND;
	public static int NULL_NUTRIMENTS;
	public static int NULL_IDS;

	public static void resetStats(){
		TOTAL_PRODUCTS = 0;
		PRODUCTS_NOT_FOUND_THROUGH_API = 0;
		IMAGES_NOT_FOUND = 0;
		NULL_NUTRIMENTS = 0;
		NULL_IDS = 0;
	}

	public static void printStats(){
		String print;
		if(OFFToProduct.getImageUrl()){
			print = "\n#######\n\nTotal = " + TOTAL_PRODUCTS;
			print += "\nNot found Products through API = " + PRODUCTS_NOT_FOUND_THROUGH_API;
			print += "\nImage not found = " + IMAGES_NOT_FOUND;
			print += "\nMissing images = " + (IMAGES_NOT_FOUND+PRODUCTS_NOT_FOUND_THROUGH_API);
			print += "\nNot Found Products/Total Products = " + PRODUCTS_NOT_FOUND_THROUGH_API*100/TOTAL_PRODUCTS + "%";
			print += "\nMissing Images/Total Products = " + (IMAGES_NOT_FOUND+PRODUCTS_NOT_FOUND_THROUGH_API)*100/TOTAL_PRODUCTS + "%";
			print += "\nNull Nutriment Lists = " + NULL_NUTRIMENTS;
			print += "\nNull ids = " + NULL_IDS;
			print += "\nNull Nutriment Lists/Total Products = " + NULL_NUTRIMENTS*100/TOTAL_PRODUCTS + "%";
			print += "\nNull ids/Total Products = " + NULL_IDS*100/TOTAL_PRODUCTS + "%";
		}else if(TOTAL_PRODUCTS != 0){
			print = "\n#######\n\nTotal Products = " + TOTAL_PRODUCTS;
			print += "\nNull Nutriment Lists = " + NULL_NUTRIMENTS;
			print += "\nNull ids = " + NULL_IDS;
			print += "\nNull Nutriment Lists/Total Products = " + NULL_NUTRIMENTS*100/TOTAL_PRODUCTS + "%";
			print += "\nNull ids/Total Products = " + NULL_IDS*100/TOTAL_PRODUCTS + "%";
		}else{
			print = "\n#######\n\nTotal Products = " + TOTAL_PRODUCTS;;
		}
		System.out.println(print);
	}

	/*
	 * Total = 6995
	 * Not found Products through API = 75
	 * Image not found = 115
	 * Missing images = 190
	 * Not Found Products/Total Products = 1%
	 * Missing Images/Total Products = 2%
	 * Null Nutriment Lists = 79
	 * Null ids = 72
	 * Null Nutriment Lists/Total Products = 1%
	 * Null ids/Total Products = 1%
	 */
}
