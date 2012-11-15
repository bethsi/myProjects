package com.filters;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.interaction.ByteResponseWrapper;

public class LetterMixingFilter implements Filter {

	protected FilterConfig config;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		ServletResponse newResponse = response;

		if (request instanceof HttpServletRequest) {
			newResponse = new ByteResponseWrapper(
					(HttpServletResponse) response);
			request.setCharacterEncoding("UTF-8");
			newResponse.setCharacterEncoding("UTF-8");
		}

		chain.doFilter(request, newResponse);

		if (newResponse instanceof ByteResponseWrapper) {
			String text = new String(((ByteResponseWrapper) newResponse).getBytes(), Charset.forName("UTF-8"));		
			StringBuilder newText = new StringBuilder("<pre>");
			// mix letters inside word
			if (text != null) {
				StringTokenizer tokenizer = new StringTokenizer(text, " \t\r,.:;?![]'"); 
				while (tokenizer.hasMoreElements()){
					String word = tokenizer.nextToken();
					
					if (word.contains("\n") && !word.equals("\n")){
						word = word.substring(1, word.length());
						newText.append("\n");
					}
					StringBuilder newWord = new StringBuilder(""); 
					if (word.length() > 1) {						
						String lettersToMix = word.substring(1,
								word.length() - 1);
						char[] array = new char[lettersToMix.length()];
						for (int j = 0; j < lettersToMix.length(); j++) {
							array[j] = lettersToMix.charAt(j);
						}
						char[] newChar = mixLetters(array);
						StringBuilder newCharString = new StringBuilder("");
						for (int j = 0; j < newChar.length; j++) {
							newCharString.append(newChar[j]);
						}
						newWord.append(word.substring(0, 1));
						newWord.append(newCharString);
						newWord.append(word.substring(word.length() - 1,
										word.length()));
					} else {
						newWord.append(word);
					}
					newText.append(newWord + " ");
				}
				newText.append("</pre>");
				response.getWriter().write(newText.toString());
			}
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

	private char[] mixLetters(char[] array) {
		char[] ch = new char[array.length];
		Random r = new Random();
		for (int i = 0; i < ch.length; i++) {
			ch[i] = '\0';
		}
		for (int i = 0; i < array.length; i++) {
			boolean ok = true;
			do {
				ok = true;
				int nr = r.nextInt(array.length);
				if (ch[nr] == '\0') {
					ch[nr] = array[i];
				} else
					ok = false;
			} while (!ok);
		}
		return ch;
	}

}
