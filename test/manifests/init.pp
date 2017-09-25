class test {


  file{"/tmp/testfile":
 ensure => "file",
   owner => 'root',
   group  => 'root',
	content => template('test/non_existent.erb')
}

  notice abs(-42)
}
