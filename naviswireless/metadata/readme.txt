If weird exceptions happen (NullPointerException from NSICom), clean up build and dist folders.
Package Java ME CDC Personal Profile Free Layout for AWT. (IBM J9)
Package Java ME CDC AGUI Free Layout for Swing. (CrE-ME)

IBM J9, add j2me_rpc_ri.jar, j2me_xml_ri.jar, midpapi21.jar to bootstrap classpath.
CrE-ME comes with midpapi21.jar, so do not package that jar in the output file, otherwise, 
you get "data starting at 112 is in unknown format".
