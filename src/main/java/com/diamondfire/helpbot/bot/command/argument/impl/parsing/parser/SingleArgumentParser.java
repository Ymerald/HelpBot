package com.diamondfire.helpbot.bot.command.argument.impl.parsing.parser;

import com.diamondfire.helpbot.bot.command.argument.impl.parsing.*;
import com.diamondfire.helpbot.bot.command.argument.impl.parsing.exceptions.*;
import com.diamondfire.helpbot.bot.command.argument.impl.parsing.types.SingleArgumentContainer;
import com.diamondfire.helpbot.bot.command.argument.impl.types.Argument;

import java.util.Deque;

public class SingleArgumentParser<A> extends ArgumentParser<SingleArgumentContainer<A>, A> {


    public SingleArgumentParser(SingleArgumentContainer<A> container) {
        super(container);
    }

    @Override
    public ParsedArgument<?> parse(String identifier, ArgumentStack stack) throws ArgumentException {
        Deque<String> args = stack.getRawArguments();
        Argument<A> arg = getContainer().getArgument();
        ParsedArgument<A> parsedArgument;

        String rawArg = args.peek();
        if (rawArg == null) {
            throw new MissingArgumentException("Expected an argument, but got nothing.");
        }
        parsedArgument = new ParsedArgument<>(identifier, arg.parseValue(rawArg));
        args.pop();

        return parsedArgument;
    }
}