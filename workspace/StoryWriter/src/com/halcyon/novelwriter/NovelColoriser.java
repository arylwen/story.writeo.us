package com.halcyon.novelwriter;

import android.text.*;
import android.text.style.*;
import android.widget.*;
import com.halcyon.storywriter.*;
import java.util.*;

	public class NovelColoriser implements TextWatcher
	{

		private EditText text;
		private StructureTemplateHelper helper;
		private CounterListener counterListener;
		private long wordsBeforeScene;

		public NovelColoriser(EditText aText, StructureTemplateHelper aHelper, 
		                      long aWordsBeforScene){
			text = aText;
			helper = aHelper;
			wordsBeforeScene = aWordsBeforScene;
		}

		public void onTextChanged(CharSequence one, int a, int b, int c) {

			Map<Integer, SNode> structure = helper.getStructure();
			List<Integer> limits = helper.getLimits();
			Map<Integer, ForegroundColorSpan> spans = helper.getSpans();

			long wordc = wordCount(text.getText().toString());	

			if(this.counterListener != null)
			{
				counterListener.onWordCountUpdate(wordc);
			}

			wordc = wordc + wordsBeforeScene;
			//color according to structure
			String stext = text.getText().toString();
			Editable s = text.getText();
			int lower = (int)wordsBeforeScene;
			int clower = 0;

			String currentPart = "Outside structure";
			
			for(int i=0; i < limits.size(); i++)
			{
				int upper = limits.get(i);
				if(upper < lower) continue; //ignore color for previous sections
				SNode current = structure.get(upper);
				if(upper < wordc)
				{
					//still inside the text
					int cupper = charCount(stext, clower, upper-lower);

					s.setSpan(spans.get(current.getId()), clower, cupper, 
							  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 

					lower = upper;
					clower = cupper-1;
				} else {
					//color up to the end of the text; break
					s.setSpan(spans.get(current.getId()), clower, stext.length(), 
							  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
                    currentPart = current.getName();
					break;
				}
			}
			
			if(this.counterListener != null)
			{
				counterListener.onPartUpdate(currentPart);
			}
		}

		// complete the interface
		public void afterTextChanged(Editable s) { }
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		public static long wordCount(String line){
			long numWords = 0;
			int index = 0;
			boolean prevWhiteSpace = true;

			while(index < line.length())
			{ 
				char c = line.charAt(index++);
				boolean currWhiteSpace = Character.isWhitespace(c);
				if(prevWhiteSpace && !currWhiteSpace)
				{numWords++;}
				prevWhiteSpace = currWhiteSpace;
			}

			return numWords;
		}

		public SpannableString getColorisedTemplateInfo()
		{
			StringBuilder sb = new StringBuilder();
			List<Integer> limits = helper.getLimits();
			List<Integer> slimits = new ArrayList<Integer>();
			for(Integer limit:limits)
			{
				SNode node = helper.getStructure().get(limit);
				sb.append(node.getName());
				sb.append("--");
				sb.append(limit.intValue());
				sb.append("\n");
				slimits.add(sb.length());
			}

			SpannableString spannablecontent=new SpannableString(sb.toString()); 
			Map<Integer, ForegroundColorSpan> spans = helper.getSpans();

			int prev = 0;
			int idx =0;
			for(Integer limit:limits)
			{
				SNode node = helper.getStructure().get(limit);
				ForegroundColorSpan span = spans.get(node.getId());
				spannablecontent.setSpan(span, prev,slimits.get(idx), 
										 SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE); 
				prev = slimits.get(idx);
				idx++;
			}

			return spannablecontent;
		}



		/**
		 determines the index of the last char for the next numWords words
		 **/
		private static int charCount(String line, int start, int numWords){
			long localNumWords = 0;
			int index = start;
			boolean prevWhiteSpace = true;

			while((index < line.length()) && (localNumWords <= numWords))
			{ 
				char c = line.charAt(index++);
				boolean currWhiteSpace = Character.isWhitespace(c);
				if(prevWhiteSpace && !currWhiteSpace)
				{
					localNumWords++;
				}
				prevWhiteSpace = currWhiteSpace;
			}

			return index;
		}

		public void setCounterListener(CounterListener aListener){
			counterListener = aListener;
		}

		interface CounterListener{
			public void onWordCountUpdate(long wordCount);
		    public void onPartUpdate(String part);
		}
	}
