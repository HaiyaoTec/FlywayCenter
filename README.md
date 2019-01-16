# Flyway Center
[ ![Download](https://api.bintray.com/packages/jinuotec/maven/flyway-center/images/download.svg?version=1.0.1) ](https://bintray.com/jinuotec/maven/flyway-center/1.0.1/link) 
[ ![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)  
Flyway Center is mainly used to add multi-environment support to Flyway

### Manager database in dedicated project
We suggest to manager database in dedicated project, because in most cases, there are multiple projects use the same database. so we made flyway center to manager the database project.  
add migration scripts in the java and resources directory.   
In java directory, you can write java based migrations.  


<image src="doc/project.jpg" width=300/>


# Usage
### Apply Plugin
Flyway Center will auto apply Flyway Plugin.

```groovy
buildscript {
   
    dependencies {
        classpath 'me.jinuo.backend:flyway-center:1.0.1'
    }
    
}

apply plugin: 'flyway-center'

```

### Config Database
You can config env in flyway_center in `build.gradle` and the config in flyway is also effective.

```groovy
flyway_center{
    driver = 'com.mysql.cj.jdbc.Driver'
    env {
        beta {
            url = 'jdbc:mysql://xxxxxxxx:3306/flyway_test'
            user = 'xxxxx'
            password = 'xxxxx'
            target = '3.1.5'
        }
        prod {
            // any operation in protected env must be confirm
            protect = true
            url = 'jdbc:mysql://xxxxxxxx:3306/flyway_prod'
            user = 'xxxxx'
            // you can don't write password here and input when invoke task
            password = 'xxxxx'
            target = '3.1.5'
        }
        other_env {
           ......
        }
    }
}
```


### Start
There will be several task created by flyway center in group `flyway center`

 
<image src="doc/tasks.jpg" width=300/>

**Example**
```java

➜  ./gradlew infoBeta   

> Task :infoBeta
Schema version: 3
+-----------+---------+----------------+------+---------------------+---------+
| Category  | Version | Description    | Type | Installed On        | State   |
+-----------+---------+----------------+------+---------------------+---------+
| Versioned | 1       | INIT           | SQL  | 2019-01-16 06:23:29 | Success |
| Versioned | 2       | ADD PEOPLE     | SQL  | 2019-01-16 06:26:44 | Success |
| Versioned | 3       | ADD PEOPEL TOO | JDBC | 2019-01-17 04:46:38 | Success |
| Versioned | 3.1.5   | ADD TABLE2     | SQL  |                     | Pending |
+-----------+---------+----------------+------+---------------------+---------+


BUILD SUCCESSFUL in 3s
4 actionable tasks: 1 executed, 3 up-to-date

```

```java

➜  ./gradlew migrateProd

Please enter 'migrate prod' to comfirm dangerous action:  (default: no): migrate prod


BUILD SUCCESSFUL in 16s
4 actionable tasks: 1 executed, 3 up-to-date

```


## License
```markdown
Copyright (C) 2018-2019 JunuoTec

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

