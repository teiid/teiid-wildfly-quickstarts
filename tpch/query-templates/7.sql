-- $ID$
-- TPC-H/TPC-R Volume Shipping Query (Q7)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	supp_nation,
	cust_nation,
	l_year,
	sum(volume) as revenue
from
	(
		select
			n1.n_name as supp_nation,
			n2.n_name as cust_nation,
			extract(year from l_shipdate) as l_year,
			l_extendedprice * (1 - l_discount) as volume
		from
			tpch1.tpch.supplier,
			tpch2.tpch.lineitem,
			tpch2.tpch.orders,
			tpch1.tpch.customer,
			tpch1.tpch.nation n1,
			tpch1.tpch.nation n2
		where
			s_supplierkey = l_supplierkey
			and o_orderskey = l_orderskey
			and c_customerkey = o_customerkey
			and s_nationkey = n1.n_nationkey
			and c_nationkey = n2.n_nationkey
			and (
				(n1.n_name = ':1' and n2.n_name = ':2')
				or (n1.n_name = ':2' and n2.n_name = ':1')
			)
			and l_shipdate between '1995-01-01' and '1996-12-31'
	) as shipping
group by
	supp_nation,
	cust_nation,
	l_year
order by
	supp_nation,
	cust_nation,
	l_year;
