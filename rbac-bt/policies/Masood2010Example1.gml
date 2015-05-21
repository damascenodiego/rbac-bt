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
        label "00001000"
    	]
    node [
        id 4
        label "00000010"
    	]
    node [
        id 5
        label "11000000"
    	]
    node [
        id 6
        label "10100000"
    	]
    node [
        id 7
        label "10001000"
    	]
    node [
        id 8
        label "10000010"
    	]
    node [
        id 9
        label "00110000"
    	]
    node [
        id 10
        label "00101000"
    	]
    node [
        id 11
        label "00100010"
    	]
    node [
        id 12
        label "00001010"
    	]
    node [
        id 13
        label "11100000"
    	]
    node [
        id 14
        label "11001000"
    	]
    node [
        id 15
        label "11000010"
    	]
    node [
        id 16
        label "10110000"
    	]
    node [
        id 17
        label "10101000"
    	]
    node [
        id 18
        label "10100010"
    	]
    node [
        id 19
        label "10001010"
    	]
    node [
        id 20
        label "00111000"
    	]
    node [
        id 21
        label "00110010"
    	]
    node [
        id 22
        label "00101010"
    	]
    node [
        id 23
        label "11101000"
    	]
    node [
        id 24
        label "11100010"
    	]
    node [
        id 25
        label "11001010"
    	]
    node [
        id 26
        label "10111000"
    	]
    node [
        id 27
        label "10110010"
    	]
    node [
        id 28
        label "10101010"
    	]
    node [
        id 29
        label "00111010"
    	]
    node [
        id 30
        label "11101010"
    	]
    node [
        id 31
        label "10111010"
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
        source 0
        target 3
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 0
        target 4
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 1
        target 0
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 1
        target 5
        label "AC(u1,r1)/grant"
    	]
    edge [
        source 1
        target 6
        label "AS(u2,r1)/grant"
    	]
    edge [
        source 1
        target 7
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 1
        target 8
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 2
        target 6
        label "AS(u1,r1)/grant"
    	]
    edge [
        source 2
        target 0
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 2
        target 9
        label "AC(u2,r1)/grant"
    	]
    edge [
        source 2
        target 10
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 2
        target 11
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 3
        target 7
        label "AS(u1,r1)/grant"
    	]
    edge [
        source 3
        target 10
        label "AS(u2,r1)/grant"
    	]
    edge [
        source 3
        target 0
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 3
        target 12
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 4
        target 8
        label "AS(u1,r1)/grant"
    	]
    edge [
        source 4
        target 11
        label "AS(u2,r1)/grant"
    	]
    edge [
        source 4
        target 12
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 4
        target 0
        label "DP(p2,r1)/grant"
    	]
    edge [
        source 5
        target 0
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 5
        target 1
        label "DC(u1,r1)/grant"
    	]
    edge [
        source 5
        target 13
        label "AS(u2,r1)/grant"
    	]
    edge [
        source 5
        target 14
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 5
        target 15
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 6
        target 2
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 6
        target 13
        label "AC(u1,r1)/grant"
    	]
    edge [
        source 6
        target 1
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 6
        target 16
        label "AC(u2,r1)/grant"
    	]
    edge [
        source 6
        target 17
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 6
        target 18
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 7
        target 3
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 7
        target 14
        label "AC(u1,r1)/grant"
    	]
    edge [
        source 7
        target 17
        label "AS(u2,r1)/grant"
    	]
    edge [
        source 7
        target 1
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 7
        target 19
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 8
        target 4
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 8
        target 15
        label "AC(u1,r1)/grant"
    	]
    edge [
        source 8
        target 18
        label "AS(u2,r1)/grant"
    	]
    edge [
        source 8
        target 19
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 8
        target 1
        label "DP(p2,r1)/grant"
    	]
    edge [
        source 9
        target 16
        label "AS(u1,r1)/grant"
    	]
    edge [
        source 9
        target 0
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 9
        target 2
        label "DC(u2,r1)/grant"
    	]
    edge [
        source 9
        target 20
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 9
        target 21
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 10
        target 17
        label "AS(u1,r1)/grant"
    	]
    edge [
        source 10
        target 3
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 10
        target 20
        label "AC(u2,r1)/grant"
    	]
    edge [
        source 10
        target 2
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 10
        target 22
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 11
        target 18
        label "AS(u1,r1)/grant"
    	]
    edge [
        source 11
        target 4
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 11
        target 21
        label "AC(u2,r1)/grant"
    	]
    edge [
        source 11
        target 22
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 11
        target 2
        label "DP(p2,r1)/grant"
    	]
    edge [
        source 12
        target 19
        label "AS(u1,r1)/grant"
    	]
    edge [
        source 12
        target 22
        label "AS(u2,r1)/grant"
    	]
    edge [
        source 12
        target 4
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 12
        target 3
        label "DP(p2,r1)/grant"
    	]
    edge [
        source 13
        target 2
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 13
        target 6
        label "DC(u1,r1)/grant"
    	]
    edge [
        source 13
        target 5
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 13
        target 23
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 13
        target 24
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 14
        target 3
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 14
        target 7
        label "DC(u1,r1)/grant"
    	]
    edge [
        source 14
        target 23
        label "AS(u2,r1)/grant"
    	]
    edge [
        source 14
        target 5
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 14
        target 25
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 15
        target 4
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 15
        target 8
        label "DC(u1,r1)/grant"
    	]
    edge [
        source 15
        target 24
        label "AS(u2,r1)/grant"
    	]
    edge [
        source 15
        target 25
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 15
        target 5
        label "DP(p2,r1)/grant"
    	]
    edge [
        source 16
        target 9
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 16
        target 1
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 16
        target 6
        label "DC(u2,r1)/grant"
    	]
    edge [
        source 16
        target 26
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 16
        target 27
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 17
        target 10
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 17
        target 23
        label "AC(u1,r1)/grant"
    	]
    edge [
        source 17
        target 7
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 17
        target 26
        label "AC(u2,r1)/grant"
    	]
    edge [
        source 17
        target 6
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 17
        target 28
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 18
        target 11
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 18
        target 24
        label "AC(u1,r1)/grant"
    	]
    edge [
        source 18
        target 8
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 18
        target 27
        label "AC(u2,r1)/grant"
    	]
    edge [
        source 18
        target 28
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 18
        target 6
        label "DP(p2,r1)/grant"
    	]
    edge [
        source 19
        target 12
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 19
        target 25
        label "AC(u1,r1)/grant"
    	]
    edge [
        source 19
        target 28
        label "AS(u2,r1)/grant"
    	]
    edge [
        source 19
        target 8
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 19
        target 7
        label "DP(p2,r1)/grant"
    	]
    edge [
        source 20
        target 26
        label "AS(u1,r1)/grant"
    	]
    edge [
        source 20
        target 3
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 20
        target 10
        label "DC(u2,r1)/grant"
    	]
    edge [
        source 20
        target 9
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 20
        target 29
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 21
        target 27
        label "AS(u1,r1)/grant"
    	]
    edge [
        source 21
        target 4
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 21
        target 11
        label "DC(u2,r1)/grant"
    	]
    edge [
        source 21
        target 29
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 21
        target 9
        label "DP(p2,r1)/grant"
    	]
    edge [
        source 22
        target 28
        label "AS(u1,r1)/grant"
    	]
    edge [
        source 22
        target 12
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 22
        target 29
        label "AC(u2,r1)/grant"
    	]
    edge [
        source 22
        target 11
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 22
        target 10
        label "DP(p2,r1)/grant"
    	]
    edge [
        source 23
        target 10
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 23
        target 17
        label "DC(u1,r1)/grant"
    	]
    edge [
        source 23
        target 14
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 23
        target 13
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 23
        target 30
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 24
        target 11
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 24
        target 18
        label "DC(u1,r1)/grant"
    	]
    edge [
        source 24
        target 15
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 24
        target 30
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 24
        target 13
        label "DP(p2,r1)/grant"
    	]
    edge [
        source 25
        target 12
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 25
        target 19
        label "DC(u1,r1)/grant"
    	]
    edge [
        source 25
        target 30
        label "AS(u2,r1)/grant"
    	]
    edge [
        source 25
        target 15
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 25
        target 14
        label "DP(p2,r1)/grant"
    	]
    edge [
        source 26
        target 20
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 26
        target 7
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 26
        target 17
        label "DC(u2,r1)/grant"
    	]
    edge [
        source 26
        target 16
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 26
        target 31
        label "AP(p2,r1)/grant"
    	]
    edge [
        source 27
        target 21
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 27
        target 8
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 27
        target 18
        label "DC(u2,r1)/grant"
    	]
    edge [
        source 27
        target 31
        label "AP(p1,r1)/grant"
    	]
    edge [
        source 27
        target 16
        label "DP(p2,r1)/grant"
    	]
    edge [
        source 28
        target 22
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 28
        target 30
        label "AC(u1,r1)/grant"
    	]
    edge [
        source 28
        target 19
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 28
        target 31
        label "AC(u2,r1)/grant"
    	]
    edge [
        source 28
        target 18
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 28
        target 17
        label "DP(p2,r1)/grant"
    	]
    edge [
        source 29
        target 31
        label "AS(u1,r1)/grant"
    	]
    edge [
        source 29
        target 12
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 29
        target 22
        label "DC(u2,r1)/grant"
    	]
    edge [
        source 29
        target 21
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 29
        target 20
        label "DP(p2,r1)/grant"
    	]
    edge [
        source 30
        target 22
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 30
        target 28
        label "DC(u1,r1)/grant"
    	]
    edge [
        source 30
        target 25
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 30
        target 24
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 30
        target 23
        label "DP(p2,r1)/grant"
    	]
    edge [
        source 31
        target 29
        label "DS(u1,r1)/grant"
    	]
    edge [
        source 31
        target 19
        label "DS(u2,r1)/grant"
    	]
    edge [
        source 31
        target 28
        label "DC(u2,r1)/grant"
    	]
    edge [
        source 31
        target 27
        label "DP(p1,r1)/grant"
    	]
    edge [
        source 31
        target 26
        label "DP(p2,r1)/grant"
    	]
]
