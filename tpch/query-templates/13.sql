-- $ID$
-- TPC-H/TPC-R Customer Distribution Query (Q13)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	c_count,
	count(*) as custdist
from
	(  select c_customerkey,
			count(o_orderskey) as c_count
		from
			tpch1.tpch.customer  left outer join tpch2.tpch.orders on
				c_customerkey = o_customerkey
				and o_comment not like '%:1%:2%'
		group by
			c_customerkey
	) as c_orders 
group by
	c_count
order by
	custdist desc,
	c_count desc;
