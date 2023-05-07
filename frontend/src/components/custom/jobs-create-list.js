import React, {useReducer} from "react";
import JobCreate from "./job-create";
import ActionButton from "./actionButton";
import AddIcon from "@material-ui/icons/Add";
import {JobCreateReducer} from "./job-create-reducer";
import axios from "axios";


export default function JobsCreateList(props) {
    const [components, dispatch] = useReducer(JobCreateReducer, [{
        rowId: 0,
        key: 0,
        jobData: {
            jobSignature: "",
            jobArg: [],
            jobDate: ""
        }
    }]);

    const addComponent = () => {
        dispatch({
            type: 'add'
        })
    }

    const removeComponent = (id) => {
        dispatch({
            type: 'delete',
            id: id
        })
    }

    function createJobs() {
        let jobs = []
        components.forEach(it => {
            if(it.jobData.jobSignature!== undefined && it.jobData.jobSignature!== "")
            jobs.push(it.jobData)
        })
        if(jobs.length>0){
            axios.post(`${process.env.REACT_APP_BACKEND_APP_URL}/api/v1/jobs`, jobs)
        }
    }

    return (
        <div>
            {components.map(item => {
                return <JobCreate rowId={item.rowId} key={item.rowId} remove={removeComponent}
                                  jobData={item.jobData}></JobCreate>
            })}

            <ActionButton background={"green"}
                          color={"white"}
                          Icon={<AddIcon/>}
                          onClick={addComponent}
            >
            </ActionButton>
            <ActionButton background={"green"}
                          color={"white"}
                          Icon={"Create jobs"}
                          onClick={createJobs}
            >
            </ActionButton>
        </div>
    )
}