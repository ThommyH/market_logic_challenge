Market Logic Software Challenge

Input (booking_requests.txt):
0900 1730
2015-08-17 10:17:06 EMP001
2015-08-21 09:00 2
2015-08-16 12:34:56 EMP002
2015-08-21 09:00 2
2015-08-16 09:28:23 EMP003
2015-08-22 14:00 2
2015-08-17 11:23:45 EMP004
2015-08-22 16:00 1
2015-08-15 17:29:12 EMP005
2015-08-21 16:00 3

Output:
2015-08-21
09:00 11:00 EMP002
2015-08-22
14:00 16:00 EMP003
16:00 17:00 EMP004

Usage:
./run_script.sh dev_build
./run_script.sh dev_run src/test/resources/booking_requests.txt
