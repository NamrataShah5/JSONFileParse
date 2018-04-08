# JSONFileParse
This program parses a file of JSON format and looks for cyclical references
If there is a node being referenced more than once (a child node referencing to a parent node), then the path is flagged as invalid and printed out as invalid. Otherwise, if a child node eventually points to null, then the path is printed out as valid.
