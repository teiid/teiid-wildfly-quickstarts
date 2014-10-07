-- $ID$
-- TPC-H/TPC-R Returned Item Reporting Query (Q10)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	c_customerkey,
	c_name,
	sum(l_extendedprice * (1 - l_discount)) as revenue,
	c_acctbal,
	n_name,
	c_address,
	c_phone,
	c_comment
from
	tpch1.tpch.customer,
	tpch2.tpch.orders,
	tpch2.tpch.lineitem,
	tpch1.tpch.nation
where
	c_customerkey = o_customerkey
	and l_orderskey = o_orderskey
	and o_orderdate >= ':1'
	and o_orderdate < TIMESTAMPADD(SQL_TSI_MONTH, '3', ':1')
	and l_returnflag = 'R'
	and c_nationkey = n_nationkey
group by
	c_customerkey,
	c_name,
	c_acctbal,
	c_phone,
	n_name,
	c_address,
	c_comment
order by
	revenue desc;
