-- $ID$
-- TPC-H/TPC-R Local Supplier Volume Query (Q5)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	n_name,
	sum(l_extendedprice * (1 - l_discount)) as revenue
from
	tpch1.tpch.customer,
	tpch2.tpch.orders,
	tpch2.tpch.lineitem,
	tpch1.tpch.supplier,
	tpch1.tpch.nation,
	tpch1.tpch.region
where
	c_customerkey = o_customerkey
	and l_orderskey = o_orderskey
	and l_supplierkey = s_supplierkey
	and c_nationkey = s_nationkey
	and s_nationkey = n_nationkey
	and n_regionkey = r_regionkey
	and r_name = ':1'
	and o_orderdate >= ':2'
	and o_orderdate < TIMESTAMPADD(SQL_TSI_YEAR,'1',':2')
group by
	n_name
order by
	revenue desc;
