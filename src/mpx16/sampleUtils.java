package mpx16;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class sampleUtils
{
	public String InNames[]=new String[16];
	public String OutNames[]=new String[16];
	public String FinalName[]=new String[16];
	public int NumberFiles=0;
	
	public String getSampleName(int idx)
	{
		return OutNames[idx];
	}
	public String getInName(int idx)
	{
		return InNames[idx];
	}
//read in all wav files from a given directory and copy 'normalised versions' to another
	public int readDirectory(String dirName,String outDir)
	{
		File dir = new File(dirName);
		File [] files = dir.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".wav");
		    }
		});
		int maxNo=16;
		if(files.length<maxNo)
			maxNo=files.length;
		
		File wavfile;
		int i;
		for(i=0;i<maxNo;i++)
		{
			wavfile=files[i];
			String wavName=wavfile.getName();
			InNames[i]=wavfile.getAbsolutePath();
//convert to outname

			String outName=createOutName(i,wavName,outDir);
			OutNames[i]=outName;
			try {
				convertWavFile(wavfile.getPath(),outName);
			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
		}	
		NumberFiles=i;
		System.out.println("No Files :"+NumberFiles);
		return 0;
	}
	private String createOutName(int idx,String inFile,String outDir)
	{
		
//first thing, split the name
		String[] splitArray = inFile.split(".wav");
		String workName1=splitArray[0];
		String workString=null;
//second thing - remove any spaces
		String workName=workName1.replaceAll("\\s+","");
//		System.out.println("Whitespace free name stem:"+workName);
		if(workName.length()<=8)
		{
			String easyFile=workName+".wav";
			workString=outDir+easyFile;
			FinalName[idx]=easyFile;
		}
		else
		{
//assume no more than 99 files of the same name, so cut the name to the first 6 characters
			workName=workName.substring(0,6);
//now find the best name
			String fileCheck="";
			boolean found=false;
			
			for(int i=0;i<99;i++)
			{
				String numSuffix=String.format("%02d",i);
				FinalName[idx]=workName+numSuffix+".wav";
				fileCheck=FinalName[idx];
				fileCheck=outDir+fileCheck;
				File f = new File(fileCheck);
				if(!f.exists())
				{
					found=true;
					break;
				}
			}
			if(found)
			{
				workString=fileCheck;
			}
			else
			{
				System.out.println("Cound not find 8.3 file name for :"+inFile);
			}
		}
		return workString;
	}
//convert incoming wav file into something that the MPX16 will play	
	public int convertWavFile(String fName,String outName) throws UnsupportedAudioFileException, IOException
	{
		File	sourceFile = new File(fName);
		File	targetFile = new File(outName);
		AudioFileFormat		sourceFileFormat = AudioSystem.getAudioFileFormat(sourceFile);
		AudioFileFormat.Type	targetFileType = sourceFileFormat.getType();
		AudioInputStream	sourceStream = null;
		sourceStream = AudioSystem.getAudioInputStream(sourceFile);
		if (sourceStream == null)
		{
			System.out.println("cannot open source audio file: " + sourceFile);
			return 1;
		}
		AudioFormat	sourceFormat = sourceStream.getFormat();
		

		float sampleRate=sourceFormat.getSampleRate();
		int sampleBits=sourceFormat.getSampleSizeInBits();
		int frameSize=sourceFormat.getFrameSize();
		float frameRate=sourceFormat.getFrameRate();
		
	
		
//check for a valid sample rate
		if(sampleRate!=44100.0F
				&&sampleRate!=48000.0F
				&&sampleRate!=32000.0F
				&&sampleRate!=22050.0F
				&&sampleRate!=11205.0F)
		{
			System.out.println("Setting Sample Rate to 44.1KHz");
			sampleRate=44100.0F;
			frameRate=sampleRate;
		}
//check for 16 bits
		if(sampleBits!=16)
		{
			sampleBits=16;
			frameSize=4;
		}

		AudioFormat	targetFormat = new AudioFormat(
				sourceFormat.getEncoding(),
				sampleRate,
				sampleBits,
				sourceFormat.getChannels(),
				frameSize,
				frameRate,
				sourceFormat.isBigEndian());
		AudioInputStream	targetStream = AudioSystem.getAudioInputStream(targetFormat, sourceStream);
		int	nWrittenBytes = 0;
	
		nWrittenBytes = AudioSystem.write(targetStream, targetFileType, targetFile);
	
		return 0;
	}
}
