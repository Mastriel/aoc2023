import networkx

lines = open('day25.txt')

graph = networkx.Graph()

nodes = []
wires = []

for line in lines:
    line = line.strip()
    label, connected = line.split(': ')
    connectedWires = connected.split(' ')
    nodes.append(label)
    for wire in connectedWires:
        wires.append(tuple([label, wire]))

graph.add_nodes_from(nodes)
graph.add_edges_from(wires)

min_cut = networkx.minimum_edge_cut(graph)

for edge in min_cut:
    graph.remove_edge(*edge)


wires = list(map(len, networkx.connected_components(graph)))

total = 1
for wire in wires:
    total *= wire

print(total)