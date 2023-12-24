from collections import namedtuple
from typing import NamedTuple
import z3

class Pos2D(NamedTuple):
    x: int
    y: int
    z: int

class FallingHail(NamedTuple):
    position: Pos2D
    velocity: Pos2D    

lines = open("day24.txt")

hail: list[FallingHail] = []

for line in lines:
    line = line.strip()
    pos, vel = line.split(" @ ")
    
    hail_shard = FallingHail(Pos2D(*map(int, pos.split(", "))), Pos2D(*map(int, vel.split(", "))))
    hail.append(hail_shard)

x, y, z, vx, vy, vz = z3.Real('x'), \
    z3.Real('y'), \
    z3.Real('z'), \
    z3.Real('vx'), \
    z3.Real('vy'), \
    z3.Real('vz')

solver = z3.Solver()

for i, hail_shard in enumerate(hail):
    pos_x, pos_y, pos_z = hail_shard.position
    vel_x, vel_y, vel_z = hail_shard.velocity
    dist_index = z3.Real(f"dist_{i}")
    solver.add(pos_x + vel_x * dist_index == x + vx * dist_index)
    solver.add(pos_y + vel_y * dist_index == y + vy * dist_index)
    solver.add(pos_z + vel_z * dist_index == z + vz * dist_index)
    if i > 3:
        # you only need to check if 3 match because 3 shards + 6 parameters leads to 9 equations and 9 total parameters
        # meaning all should be satisfied after only these shards
        break

solver.check()
data = solver.model()

# i dont know z3 datatypes so we're doing this stupid style
convert = lambda x: int(str(data[x]))

print(convert(x) + convert(y) + convert(z))