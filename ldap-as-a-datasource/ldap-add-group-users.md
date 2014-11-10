## Steps for adding Groups and User in OpenLDAP

Assuming OpenLDAP be installed and configured correct, in this section, we will create Group `HR` and 3 Users under Group `HR`.

* Create Group HR

Create an LDIF file named hr.ldif, with the content as below:

~~~
dn: ou=HR,dc=example,dc=com
objectClass: top
objectClass: organizationalunit
ou: HR
~~~

Run ldapadd command add Group entry to directory:

~~~
ldapadd -x -D "cn=Manager,dc=example,dc=com" -w redhat -f hr.ldif
~~~

* Create User under Group HR

Create an LDIF file named hr1.ldif, with the content as below:

~~~
dn: uid=hr1,ou=HR,dc=example,dc=com
sn: name
givenName: name
objectClass: top
objectClass: person
objectClass: organizationalPerson
objectClass: inetOrgPerson
objectClass: posixAccount
objectClass: shadowAccount
gidNumber: 6592
uidNumber: 19920
mail: hr1@mail.com
uid: hr1
cn: HR One
homeDirectory: /home/hr1
gecos: hr1
loginShell: /bin/bash
shadowLastChange: 15807
userPassword: redhat
~~~

Run ldapadd command add User hr1 entry to directory:

~~~
ldapadd -x -D "cn=Manager,dc=example,dc=com" -w redhat -f hr1.ldif
~~~

Repeat the same steps to add User hr2 and hr3. Now all Group and Users be added, the following command will list all entries:

~~~
ldapsearch -x
~~~

