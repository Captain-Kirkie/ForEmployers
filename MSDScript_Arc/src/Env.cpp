//
// Created by Kirk Hietpas on 3/28/21.
//
#include "Cont.h"
#include "Env.h"
#include "Val.h"
#include "pointer.h"
// empty env
PTR(Env)Env::empty = NEW(EmptyEnv)();

/**
 *
 * @param find_name variable name to look for
 * @return  EmptyEnv has no bound var, throw error
 */
PTR(Val)EmptyEnv::lookup(std::string find_name) { //TODO: is this wrong?
    throw std::runtime_error("free variable: " + find_name);
}

EmptyEnv::EmptyEnv() {
    this->extendedEnv = nullptr; // ??
}
/**
 *
 * @param other Comparison Env
 * @return wether two emptyEnv's are equal
 */
bool EmptyEnv::equals(PTR(Env)other) {
    PTR(EmptyEnv) casted_env = CAST(EmptyEnv)(other);
    if(casted_env == nullptr)
        return false;
    return true;
}

// extended env
/**
 *
 * @param find_name variable to search for
 * @return if found variable, return the associated val, else continue on to the rest of the Env
 */
PTR(Val)ExtendedEnv::lookup(std::string find_name) {
    if (find_name == name)
        return val;
    else
        return rest->lookup(find_name); // if didnt find it, look in the rest of the environment
}
/**
 *
 * @param name holds var name
 * @param val1  value associate with the name
 * @param rest pointer to the rest of the environment
 */
ExtendedEnv::ExtendedEnv(std::string name, PTR(Val)val1, PTR(Env)rest) {
    this->name = name;
    this->val = val1;
    this->rest = rest;
}

/**
 *
 * @param other comparision Env
 * @return whether or not these two envs are equal
 */
bool ExtendedEnv::equals(PTR(Env)other) {
    PTR(ExtendedEnv)casted_env = CAST(ExtendedEnv)(other);
    if (casted_env == nullptr)
        return false;
    return this->name == casted_env->name
           && this->val->equals(casted_env->val)
           && this->rest->equals(casted_env->rest);
}



