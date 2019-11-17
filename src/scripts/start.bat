set currentDir=%cd%
mkdir -p %currentDir%/../logs
set JAVA_CLASSPATH=-classpath %currentDir%/../conf/;%currentDir%/../lib/*; -Dlogs.dir=%currentDir%/../logs
java %JAVA_CLASSPATH% com.himanshu.countryblotter.Main