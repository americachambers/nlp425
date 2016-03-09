#!/usr/bin/perl
use strict;

#-------------------------------------------------------------------
# This script performs the following actions:
#  1. Removes the comment at the top of each conversation file
#  2. Scrubs the actual conversations
#  3. Writes the scrubbed conversations to a new directory
#
# alchambers
# 3/2016
#-------------------------------------------------------------------

die "Usage: scrub.pl [path/to/data]" unless $#ARGV+1 == 1;
my $path = $ARGV[0];
$path =~ s/\/$//;

#------------------------------------------------
#	Create output directory for processed files
#------------------------------------------------
if(! -d "$path/scrubbed/"){
	mkdir("$path/scrubbed/");
}

#-----------------------------------------------------
#	Process each directory
#-----------------------------------------------------
my @directories = <$path/sw*utt>;

foreach my $dir (@directories){	
	my @files = <$dir/*.utt>;				# get a listing of files in directory		
	
	my $outdir = $dir;
	$outdir =~ s/$path\///;					# strip off full path to directory	
	$outdir = "$path/scrubbed/$outdir";		# make the corresponding output directory
	mkdir("$outdir");						
	
	print "DIR: $dir\n";
	print "OUTDIR: $outdir\n";

	foreach my $file (@files){

		#-------------------------------
		#	Open input and output files
		#-------------------------------
		my $outfile = $file;
		$outfile =~ s/$dir\///;
		$outfile = "$outdir/$outfile";		
		print "\tOUTFILE: $outfile\n";

		open(FOUT, ">", "$outfile");
		open(FIN, "<", $file);

		#-------------------------------------------
		#	Consume file header
		#------------------------------------------
		<FIN>; # *x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*
		<FIN>; # *x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*
		<FIN>; # *x*                                                                     *x*
		<FIN>; # *x*            Copyright (C) 1995 University of Pennsylvania            *x*
		<FIN>; # *x*                                                                     *x*
		<FIN>; # *x*    The data in this file are part of a preliminary version of the   *x*
		<FIN>; # *x*    Penn Treebank Corpus and should not be redistributed.  Any       *x*
		<FIN>; # *x*    research using this corpus or based on it should acknowledge     *x*
		<FIN>; # *x*    that fact, as well as the preliminary nature of the corpus.      *x*
		<FIN>; # *x*                                                                     *x*
		<FIN>; # *x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*
		<FIN>; # *x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*
		<FIN>; #
		<FIN>; #
		<FIN>; # FILENAME:       4325_1632_1519
		<FIN>; # TOPIC#:         323
		<FIN>; # DATE:           920323
		<FIN>; # TRANSCRIBER:    glp
		<FIN>; # UTT_CODER:      tc
		<FIN>; # DIFFICULTY:     1
		<FIN>; # TOPICALITY:     3
		<FIN>; # NATURALNESS:    2
		<FIN>; # ECHO_FROM_B:    1
		<FIN>; # ECHO_FROM_A:    4
		<FIN>; # STATIC_ON_A:    1
		<FIN>; # STATIC_ON_B:    1
		<FIN>; # BACKGROUND_A:   1
		<FIN>; # BACKGROUND_B:   2
		<FIN>; # REMARKS:        None.
		<FIN>; #
		<FIN>; # =========================================================================
		<FIN>; #
		<FIN>; #
		

		#---------------------------------------------------------------------------------
		#	Now read in the remaining lines of the file which are the actual conversation
		# 	and print them to the outfile
		#---------------------------------------------------------------------------------
		while(my $line = <FIN>){
			if($line =~ /^(.+?)\s+(.+?)$/){
				my $act = $1;	# the dialogue act
				my $line = $2;	# the speaker and utterance

				$line =~ /(.+?):(.+)/;							
				my $head = $1;	# the speaker information
				my $utt = $2;	# the actual utterance

				# Clean up the speaker
				$head =~ s/^@//;

				# Scrub the conversation
				$utt = scrub_text($utt);				
				print FOUT $act . "\t" . $head . "\t" . "$utt" . "\n";
			}
		}
		close(FOUT);
		close(FIN);
	}
}



#--------------------------------
#	Cleans up the text
#--------------------------------
sub scrub_text{
	my $utt = shift(@_);
	
	$utt =~ s/<<.+?>>//g;
	$utt =~ s/<.+?>//g;

	$utt =~ s/\{D(.+?)\}/$1/g;
	$utt =~ s/\{F.+?\}//g;
	$utt =~ s/\{C(.+?)\}/$1/g;
	$utt =~ s/\{E(.+?)\}/$1/g;
	$utt =~ s/\{A(.+?)\}/$1/g;
	$utt =~ s/\[(.+?)\+(.+?)\]/$2/g;

	$utt =~ s/\{A//g;
	$utt =~ s/\{C//g;

	$utt =~ s/^\s+//;
	$utt =~ s/\s+$//;
	
	$utt =~ s/\/$//;
	$utt =~ s/\+$//g;
	$utt =~ s/\-$//g;
	$utt =~ s/[\[\]\-\+]+/ /g;

	$utt =~ s/^\s+//;
	$utt =~ s/\s+$//;
	$utt =~ s/\s+/ /g;

	#$utt =~ s/\(\(\s+\)\)//g;
	return $utt;
}

