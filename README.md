**Order book assessment**
**a. Efficiency Mechanisms**
Decided to use a sorted set as orders will be stored in sorted order, first on price then on creation time.
This will ensure that searching is faster as it is faster to search sorted fata. 
**b. Solution approach**
**The Order book should keep all distinct orders ordered on their price level**
I decided to use a sorted set for this as a set does not store duplicates and I implemented
the compareTo method to use price to ensure that any interaction with the set results in 
a sorted dataset by price.
**When an Order is added a unique id should be generated for each order**
I opted to use java in built UUID unique id generator to ensure distinct Ids each time. 
I create and set this Id in the constructor of order objects to ensure that a suer will never be able 
to set it manually. To further ensure integrity of the id I did not add a set methods as this value is 
'system' generated.
**The delete operation should take as input the order id**
amended the delete method to take in an Order instead to have the side and only remove the order 
from the corresponding set.
**The modify operation should only allow for modification of the quantity and take in as parameters 
the id and the new quantity, this will cause losing the priority of the original order**
As modification should result in change of priority of the orders I decided to rather delete the order
that is being modified, Which will result in a sorted set again, then insert a new order copying unchanged 
values from the old order causing to reestablish a new priority. 

**c. Data Structures**
Sorted set - set does not allow duplicates as required and since it is sorted
it will store elements in a sorted order as defined by the overridden compareTo method.
Also, a sorted set allows for fast access to elements based on their values, 
due to the underlying hash table structure.
