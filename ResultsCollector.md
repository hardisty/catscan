**Note** This documentation corresponds to the most recent binary release, please access it from our front page. http://code.google.com/p/catscan/

# Details #

You should run ResultsCollector by double-clicking the appropriate .bat file, or by entering the name of the bat file at the command line.

ResultsCollector will compile all the files in the "results" directory which should be in the same directory as the bat file, xp directory, events directory, and others.

ResultsCollector produces the following files (in alphabetical order):

  1. **icon\_removals.csv**. This is a comma-separated list of all instances in which icons were removed, along with the number of times that icon was removed.
  1. **overall\_order.csv**. This is a comma-separated list of the average place in the order in which each icon was chosen.
  1. **overall\_orderTopoClass.csv**. This is a comma-separated list of the average place in the order in which each icon was chosen by topological equivalence class. If you see the entry "NONE", this means that some of your icons did not have a topological equivalence class, or that your classes were not recognized from the icon names. In this case, please contact the software maintainer, Frank Hardisty.
  1. **results\_summary.prn**. This is a tab-delimited file that lists the following fields for each subject: number, File-number,Gender (0=male), age, number of groups, time to group.
  1. **times\_first.csv**. This is a comma-separated list how many times each icon was selected first in its group.
  1. **timeToChose.csv**. This is a comma-separated list how long it took for each icon to be used by topological equivalence class, on average. If you see the entry "NONE", this means that some of your icons did not have a topological equivalence class, or that your classes were not recognized from the icon names. In this case, please contact the software maintainer, Frank Hardisty.

If any errors are discovered in either this documentation or in the operation of the ResultsCollector, please contact Frank Hardisty immediately.