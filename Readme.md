# Destructuring assignment generator

Command line tool to compress variable assignments JavaScript to destructuring assignments where possible.

For example:

    a = arr[0];
    b = arr[1];
    c = arr[2];
    d = arr[3];
will be converted to:

    [a, b, c, d] = arr;


# Build instructions

Download newest version of [rhino](https://github.com/mozilla/rhino) (as a jar).

Clone this repo and make a Java project,
setting `de.maxi_seitz.destructuringassigner.Main` as your main class.
Include `rhino` as a library, and build the project to a jar.


# Usage

Run in command line with two parameters:

    source_file target_file
