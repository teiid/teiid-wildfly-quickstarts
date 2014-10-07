-- $ID$
-- TPC-H/TPC-R Shipping Priority Query (Q3)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	l_orderskey,
	sum(l_extendedprice * (1 - l_discount)) as revenue,
	o_orderdate,
	o_shippriority
from
	tpch1.tpch.customer,
	tpch2.tpch.orders,
	tpch2.tpch.lineitem
where
	c_mktsegment = ':1'
	and c_customerkey = o_customerkey
	and l_orderskey = o_orderskey
	and o_orderdate < ':2'
	and l_shipdate > ':2'
group by
	l_orderskey,
	o_orderdate,
	o_shippriority
order by
	revenue desc,
	o_orderdate;
