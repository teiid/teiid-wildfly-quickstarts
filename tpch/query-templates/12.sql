-- $ID$
-- TPC-H/TPC-R Shipping Modes and Order Priority Query (Q12)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	l_shipmode,
	sum(case
		when o_orderpriority = '1-URGENT'
			or o_orderpriority = '2-HIGH'
			then 1
		else 0
	end) as high_line_count,
	sum(case
		when o_orderpriority <> '1-URGENT'
			and o_orderpriority <> '2-HIGH'
			then 1
		else 0
	end) as low_line_count
from
	tpch2.tpch.orders,
	tpch2.tpch.lineitem
where
	o_orderskey = l_orderskey
	and l_shipmode in (':1', ':2')
	and l_commitdate < l_receiptdate
	and l_shipdate < l_commitdate
	and l_receiptdate >= ':3'
	and l_receiptdate < TIMESTAMPADD(SQL_TSI_YEAR,'1',':3') 
group by
	l_shipmode
order by
	l_shipmode;
