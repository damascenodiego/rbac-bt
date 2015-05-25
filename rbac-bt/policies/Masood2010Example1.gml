graph [
    directed 1
   	id 1
    label "rbacViaAdminCommand"
    node [
        id 0
        label "00000000"
    	]
    node [
        id 1
        label "10000000"
    	]
    node [
        id 2
        label "00100000"
    	]
    node [
        id 3
        label "11000000"
    	]
    node [
        id 4
        label "10100000"
    	]
    node [
        id 5
        label "00110000"
    	]
    node [
        id 6
        label "11100000"
    	]
    node [
        id 7
        label "10110000"
    	]
    edge [
        source 0
        target 1
        label "AS(u1,r1)/grant"
    	]
    edge [
        source 0
        target 2
        label "AS(u2,r1)/grant"
    	]
    edge [
        source 1
        target 0
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 1
        target 3
        label "AC(u1,r1)/grant"
    	]
    edge [
        source 1
        target 4
        label "AS(u2,r1)/grant"
    	]
    edge [
        source 2
        target 4
        label "AS(u1,r1)/grant"
    	]
    edge [
        source 2
        target 0
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 2
        target 5
        label "AC(u2,r1)/grant"
    	]
    edge [
        source 3
        target 0
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 3
        target 1
        label "DC(u1,r1)/grant"
    	]
    edge [
        source 3
        target 6
        label "AS(u2,r1)/grant"
    	]
    edge [
        source 4
        target 2
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 4
        target 6
        label "AC(u1,r1)/grant"
    	]
    edge [
        source 4
        target 1
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 4
        target 7
        label "AC(u2,r1)/grant"
    	]
    edge [
        source 5
        target 7
        label "AS(u1,r1)/grant"
    	]
    edge [
        source 5
        target 0
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 5
        target 2
        label "DC(u2,r1)/grant"
    	]
    edge [
        source 6
        target 2
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 6
        target 4
        label "DC(u1,r1)/grant"
    	]
    edge [
        source 6
        target 3
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 7
        target 5
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 7
        target 1
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 7
        target 4
        label "DC(u2,r1)/grant"
    	]
]
