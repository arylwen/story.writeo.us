package us.writeo.novelwriter;

import android.text.*;
import java.text.*;
import java.util.*;
import us.writeo.common.novel.model.*;
import us.writeo.common.*;

public class WordCounterManager
{
     private List<Counter> wordCounterList;
     public WordCounterManager(List<Counter> aWordCounterList){
		 wordCounterList = aWordCounterList;
	 }
	 
	 public List<Counter> getWordCounterList(){
		 return wordCounterList;
	 }
	 
	 public void updateWordCounterInfo(long newWordCount){
		 
		 Date now = Calendar.getInstance().getTime();
		 now.setHours(0);
		 now.setMinutes(0);
		 now.setSeconds(0);
		 
		 if(wordCounterList.size() > 0){
			 Counter wc = wordCounterList.get(wordCounterList.size()-1);
	
			 wc.setValue(newWordCount);
			 if(!DateUtils.isSameDay(now, wc.getDate())){
				 Counter wcn = new Counter();
				 wcn.setDate(now);
				 wcn.setValue(newWordCount);
				 wordCounterList.add(wcn);
			 }
		 } else {
			 Counter wcn = new Counter();
			 wcn.setDate(now);
			 wcn.setValue(newWordCount);
			 wordCounterList.add(wcn);
		 }
	 }
	 
	 public SpannableString getColorisedWordCountInfo()
	 {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd yy");
		
		List<Integer> slimits = new ArrayList<Integer>();
		for( Counter wordCounter:wordCounterList )
		{
			
			sb.append(sdf.format(wordCounter.getDate()));
			sb.append("-");
			sb.append(wordCounter.getValue());
			sb.append("\n");
			slimits.add(sb.length());
		}

		SpannableString spannablecontent=new SpannableString(sb.toString()); 
		//Map<Integer, ForegroundColorSpan> spans = helper.getSpans();

		int prev = 0;
		int idx =0;
		for( Counter wordCounter:wordCounterList )
		{
			//SNode node = helper.getStructure().get(limit);
			//ForegroundColorSpan span = spans.get(node.getId());
			//spannablecontent.setSpan(span, prev,slimits.get(idx), 
									 //SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE); 
			prev = slimits.get(idx);
			idx++;
		}

		return spannablecontent;
	}
}
