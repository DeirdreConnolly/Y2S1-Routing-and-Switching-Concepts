// Lab 01 -- Configuring Basic Switch Settings

Switch> 			en											// change from global to privileged EXEC mode
Switch#				conf t
Switch(config)#		hostname S1
S1(config)#			enable secret class
S1(config)#			banner motd #
S1(config)#			exit
S1#					exit

S1#					conf t
S1(config)#			vlan 99										// create VLAN 99
S1(config-vlan)#	interface vlan99
S1(config-if)#		ip address 192.168.1.2 255.255.255.0		// must be configured before you can remotely access
S1(config-if)#		no shutdown
S1(config-if)#		no ip domain-lookup
S1(config-if)#		exit
S1(config)#			interface range f0/1-24, g0/1-2				// assign all user ports to VLAN 99					

S1#					show vlan brief								// verify that all user ports are in VLAN 99

S1(config)#			ip default-gateway 192.168.1.1

S1(config)#			line con 0									// restrict console port access
S1(config-line)#	password cisco
S1(config-line)#	login										// require password when logging in
S1(config-line)#	exit

S1(config)#			line vty 0 15								// configure virtual terminal (vty) lines to allow Telnet access
S1(config-line)#	password cisco
S1(config-line)#	login
S1(config-line)#	end

S1#					show running-config							// show running configuration

S1#					show interface vlan 99						// verify VLAN 99 settings

S1#					copy running-config startup-config			// save configuration

S1#					show flash									// initialise and reload switch
S1#					delete vlan.dat
S1#					erase startup-config
S1#					reload



// Lab 02 -- Configuring VLANs and Trunking

S1(config)#			vlan 10										// create VLANs
S1(config-vlan)#	name Student
S1(config-vlan)#	vlan 20
S1(config-vlan)#	name Faculty
S1(config-vlan)#	vlan 99
S1(config-vlan)#	name Management
S1(config-vlan)#	end

S1#					show vlan

S1(config)#			interface f0/6								// assign VLANS to correct switch interfaces
S1(config-if)#		switchport mode access
S1(config-if)#		switchport access vlan 10

S1(config)#			interface vlan 1
S1(config-if)#		no ip address
S1(config-if)#		interface vlan 99
S1(config-if)#		ip address 192.168.1.1 255.255.255.0

S1#					show vlan brief

S1(config-)#		interface range f0/11-24					// assign VLAN to multiple interfaces
S1(config-if)#		switchport mode access
S1(config-if)#		switchport access vlan 10
S1(config-if)#		end

S1(config)#			interface f0/24
S1(config-if)#		no switchport access vlan					// remove VLAN from interface
S1(config-if)#		end

S1(config)#			interface f0/24
S1(config-if)#		switchport access vlan 30					// create VLAN without issuing VLAN command

S1(config)#			no vlan 30									// remove VLAN from database
S1(config)#			end

S1(config)#			interface f0/1
S1(config-if)#		switchport mode dynamic desirable			// set to negotiate trunk mode

S1#					show interfaces trunk

S1(config)#			interface f0/1
S1(config-if)#		switchport mode trunk						// manually configure trunk mode



// Lab 03 -- Implementing VLAN Security

S1(config-#			interface f0/1
S1(config-if)#		switchport mode trunk
S1(config-if)#		switchport trunk native vlan 99				// configure native VLAN
S1(config-if)#		switchport nonegotiate						// turn off negotiation
S1(config-if)#		switchport trunk allow vlan 10,99			// only allow VLANs listed

S1(config)#			interface f0/1-5
S1(config-if)#		switchport mode access						// disable trunking
S1(config-if)#		switchport access vlan 999



// Lab 04 -- Configuring Trunk-Based Inter-VLAN Routing

S1(config)#			vlan 10
S1(config-vlan)#	name Students
S1(config-vlan)#	vlan 20
S1(config-vlan)#	name Faculty
S1(config-vlan)#	exit
S1(config)#			interface f0/1
S1(config-if)#		switchport mode trunk
S1(config-if)#		interface f0/5
S1(config-if)#		switchport mode trunk
S1(config-if)#		interface f0/6
S1(config-if)#		switchport mode access
S1(config-if)#		switchport access vlan 10

S2(config)#			vlan 10
S2(config-vlan)#	name Students
S2(config-vlan)#	vlan 20
S2(config-vlan)#	name Faculty
S2(config)#			interface f0/1
S2(config-if)#		switchport mode trunk
S2(config-if)#		interface f0/18
S2(config-if)#		switchport mode access
S2(config-if)#		switchport access vlan 20

R1(config)#			interface g01.1								// create subinterface
R1(config-subif)#	encapsulation dot1Q 1
R1(config-subif)#	ip address 192.168.1.1 255.255.255.0
R1(config-subif)#	interface g0/1.10
R1(config-subif)#	encapsulation dot1Q 10
R1(config-subif)#	ip address 192.168.10.1 255.255.255.0
R1(config-subif)#	interface g0/1.20
R1(config-subif)#	encapsulation dot1Q 20
R1(config-subif)#	ip address 192.168.20.1 255.255.255.0
R1(config-subif)#	exit
R1(config)#			interface g0/1
R1(config-if)#		no shutdown



// Lab 05 -- Configuring IPv4 Static and Default Routes

--> Ensure all ports are turned on

R3(config)#			interface s1/0
R3(config-if)#		ip address 10.1.1.2 255.255.255.252
R3(config-if)#		clock rate 128000							// set clock rate
R3(config-if)#		no shutdown

R1(config)#			ip route 192.168.1.0 255.255.255.0 10.1.1.2	// configure recursive static route

R3(config)#			ip route 192.168.0.0 255.255.255.0 s1/0		// configure directly connected static route

R1(config)#			ip route 0.0.0.0 0.0.0.0 s1/0				// configure default route

R1(config)#			interface loopback 0
R1(config-if)#		ip address 192.168.0.1 255.255.255.0		// configure loopback interface



// Lab 07 -- Configuring Basic RIPv2

--> Use generic routers (need serial interfaces)

R1#					conf t
R1(config)#			router rip									// configure RIP
R1(config-router)#	version 2
R1(config-router)#	passive-interface f0/0						// stops routing updates (prevents unnecessary traffic)
R1(config-router)#	network 172.30.0.0
R1(config-router)#	network 10.0.0.0

R1#					show ip protocols
R1#					debug ip rip
R1#					undebug all

R1(config)#			router rip
R1(config-router)#	no auto-summary								// turn off auto-summarisation

R1#					clear ip route *							// clear routing table

R2(config)#			ip route 0.0.0.0 0.0.0.0 209.165.201.2		// create static route
R2(config)#			router rip
R2(config-router)#	default-information originate				// advertise route to other routes



// Lab 08 -- Configuring Basic Single-Area OSPFv2

R1(config)#			router ospf 1								// configure OSPF

R1(config-router)#	network 192.168.1.0 0.0.0.255 area 0
R1(config-router)#	network 192.168.12.0 0.0.0.3  area 0
R1(config-router)#	network 192.168.13.0 0.0.0.3  area 0

R1#					show ip ospf neighbor
R1#					show ip route
R1#					show ip protocols
R1#					show ip ospf
R1#					show ip ospf brief
R1#					show ip ospf interface

R1#(config)#		interface lo0								// change router ID using loopback addresses
R1#(config-if)#		ip address 1.1.1.1 255.255.255.255

R1#					copy running-config startup-config			// save configuration

R1#					show flash									// initialise and reload router
R1#					delete vlan.dat
R1#					erase startup-config
R1#					reload

R1(config)#			router ospf 1	
R1(config-router)#	router-id 11.11.11.11						// change router ID
R1#					clear ip ospf process

R1#					show ip ospf interface f0/0
R1(config)#			router ospf 1
R1(config-router)#	passive-interface f0/0
R1#					show ip ospf interface f0/0

R1(config)#			router ospf 1
R1(config-router)#	passive-interface default					// for all OSPF interfaces

R1(config)#			interface s1/0
R1(config-if)#		bandwidth 128