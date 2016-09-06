package org.hisrc.zugradarscraper.args4j.spi;

import java.time.LocalDate;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OneArgumentOptionHandler;
import org.kohsuke.args4j.spi.Setter;

public class LocalDateOptionHandler extends OneArgumentOptionHandler<LocalDate>{

	public LocalDateOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super LocalDate> setter) {
		super(parser, option, setter);
	}

	@Override
	protected LocalDate parse(String argument) throws NumberFormatException, CmdLineException {
		return LocalDate.parse(argument);
	}
}
