package mpx16;

import java.io.File;


//import tester.pack.SimpleAdd;

public class MainClass {

	
	public static void main(String[] args) {

		sampleUtils theSampleUtils=new sampleUtils();
//read WAVs from one directory and copy them to the root of the SD card
	
		if(args.length<3)
		{
			if(args.length==0 || !args[0].equals("check"))
			{
				System.out.println("Usage is: ");
				System.out.println("  <kit no> <directory of samples> <target drive> [del - delete existing samples from root of target drive]");
				System.out.println("\nIt will copy 16 samples from the source directory into the root of the target drive");
				System.out.println("convert them to a format readable by the MPX16 and create a kit file of number <kit no>");
				System.out.println("\ni.e.\n   1 c:\\nofdrive d");
				System.out.println("will copy 16 samples from c:\\nofdrive to the root of the d drive and create a KIT001 file");
				return;
			}
		}
		
		String outDir=args[2]+":\\";
		
		//check for del flag		
		if(args.length==4 && args[3].equals("del"))
		{
			System.out.println("\nDeleting existing samples in root of target drive : "+outDir);
			File folder = new File(outDir);
			File fList[] = folder.listFiles();
			for (int i = 0; i < fList.length; i++)
			{
				if(fList[i].getName().endsWith(".wav"))
					fList[i].delete();
			}
		}
		System.out.println("\n\ncopying samples from "+args[1]+" to SD card at "+args[2]+" to kit number :"+args[0]);
		theSampleUtils.readDirectory(args[1],outDir);
	
		for(int i=0;i<theSampleUtils.NumberFiles;i++)
		{
			System.out.println(" Sample "+(i+1) +" "+ theSampleUtils.getInName(i)+" -> "+ theSampleUtils.getSampleName(i));
		}

		createKit theKit=new createKit(theSampleUtils);
		theKit.createKitFile(Integer.parseInt(args[0]),outDir);
	
		
		//	theKit.getChkSum("c:\\dev\\eclipse\\workspace\\mpx16\\kit001.kit",0);
	//	theKit.getChkSum("c:\\dev\\eclipse\\workspace\\mpx16\\kit002.kit",1);
	//	theKit.getChkSum("c:\\dev\\eclipse\\workspace\\mpx16\\kit003.kit",2);
//		theKit.getChkSum("c:\\dev\\eclipse\\workspace\\mpx16\\kit004.kit",3);
//		theKit.getChkSum("c:\\dev\\eclipse\\workspace\\mpx16\\kit005.kit",4);
//		theKit.getChkSum("c:\\dev\\eclipse\\workspace\\mpx16\\kit006.kit",5);
	//	theKit.getChkSum("c:\\dev\\eclipse\\workspace\\mpx16\\kit007.kit",6);
//		theKit.getChkSum("c:\\dev\\eclipse\\workspace\\mpx16\\kit008.kit",7);
//		theKit.getChkSum("c:\\dev\\eclipse\\workspace\\mpx16\\kit009.kit",8);
//		theKit.getChkSum("c:\\dev\\eclipse\\workspace\\mpx16\\kit010.kit",9);

//		theKit.getChkSum("c:\\dev\\eclipse\\workspace\\mpx16\\kit011.kit",10);
		
		
//		theKit.matchChkSum(11);
	}

}
