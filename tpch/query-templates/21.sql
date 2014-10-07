-- $ID$
-- TPC-H/TPC-R Suppliers Who Kept Orders Waiting Query (Q21)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	s_name,
	count(*) as numwait
from
	tpch1.tpch.supplier,
	tpch2.tpch.lineitem l1,
	tpch2.tpch.orders,
	tpch1.tpch.nation
where
	s_supplierkey = l1.l_supplierkey
	and o_orderskey = l1.l_orderskey
	and o_orderstatus = 'F'
	and l1.l_receiptdate > l1.l_commitdate
	and exists (
		select
			*
		from
			tpch2.tpch.lineitem l2
		where
			l2.l_orderskey = l1.l_orderskey
			and l2.l_supplierkey <> l1.l_supplierkey
	)
	and not exists (
		select
			*
		from
			tpch2.tpch.lineitem l3
		where
			l3.l_orderskey = l1.l_orderskey
			and l3.l_supplierkey <> l1.l_supplierkey
			and l3.l_receiptdate > l3.l_commitdate
	)
	and s_nationkey = n_nationkey
	and n_name = ':1'
group by
	s_name
order by
	numwait desc,
	s_name;
