package org.model.board;

public class LiveTranscript
{

	StringBuilder transcript;

	LiveTranscript()
	{
		 this.transcript = new StringBuilder();
	}

	LiveTranscript appendLine(String line)
	{
		transcript.append(line + "\n");
		return this;
	}

	boolean isEmpty()
	{
		return transcript.isEmpty();
	}

	@Override
	public String toString()
	{
		return transcript.toString();
	}
}
